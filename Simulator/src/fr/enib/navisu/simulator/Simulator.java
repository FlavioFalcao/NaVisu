package fr.enib.navisu.simulator;

import fr.enib.navisu.common.event.EventProvider;
import fr.enib.navisu.simulator.events.SimulatorEvent;
import fr.enib.navisu.simulator.events.SimulatorEventListener;
import gov.nasa.worldwind.formats.gpx.GpxTrack;
import gov.nasa.worldwind.formats.gpx.GpxTrackSegment;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.tracks.Track;
import gov.nasa.worldwind.tracks.TrackPoint;
import gov.nasa.worldwind.tracks.TrackPointImpl;
import gov.nasa.worldwind.tracks.TrackSegment;
import java.util.*;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author Thibault PENSEC & Jordan MENS
 * @date 22/05/2012
 */
public class Simulator implements EventProvider<SimulatorEventListener> {

    public static final double KNOT_TO_KMH = 1.852;
    public static final int NAUTICAL_MILE_TO_METERS = 1852;
    
    private static InputOutput IO = null;
    // Events
    private ArrayList<SimulatorEventListener> positionListeners;
    private SimulatorEvent event;
    // Timer
    private Timer timer;
    private RemindTask task;
    private int period; // Time in milliseconds between successive task executions
    private boolean isRunning;
    private boolean pause;
    // Positions
    private List<Position> positions;
    private List<Position> trajectory;
    private int indexPos;
    private boolean allowInterpolation;
    // Interpolation type
    public static final int INTERPOLATION_LINEAR = 0x1111;
    public static final int INTERPOLATION_GREAT_CIRCLE = 0x2222;
    public static final int INTERPOLATION_RHUMB = 0x3333;
    private int interpolationType;
    // Params
    private int knotSpeed;
    
    public Simulator() {
        this(null);
    }
    
    public Simulator(Collection<? extends Position> positions) {
        
        if(positions != null) {
            this.positions = new ArrayList<>(positions);
        } else {
            this.positions = new ArrayList<>();
        }
        
        this.positionListeners = new ArrayList<>();
        this.event = new SimulatorEvent(this);
        
        this.period = 20; // Default period
        this.isRunning = false;
        this.trajectory = new LinkedList<>();
        this.allowInterpolation = true;
    }
    
    private class RemindTask extends TimerTask {
        
        @Override
        public void run() {
            
            if(!pause) { // When pause, nothing happened
                if(indexPos < trajectory.size()) {

                    event.setOldPosition(event.getNewPosition());
                    event.setNewPosition(trajectory.get(indexPos++));
                    fireEventListeners();

                } else {
                    stop();
                }
            }
        }
    }
    
    public void start() {
        
        if(!pause) {
            getIO().getOut().println("[START]");
            if(positions.size() < 2) {
                getIO().getErr().println("[Simulator][start] ERROR : there must be at least 2 positions !");
                return;
            }
            
            timer = new Timer();
            task = new RemindTask();
            isRunning = true;
            pause = false;
            indexPos = 0;
            event.setIsLastPosition(false);
            event.setNewPosition(positions.get(0));
            makeTrajectory(); // Calcul the trajectory
            timer.schedule(task, 0, period); // Start the timer
        } else {
            getIO().getOut().println("[RESTART]");
            pause = false;
        }
    }
    
    public void stop() {
        getIO().getOut().println("[STOP]");
        if(timer != null) {
            timer.cancel();
            timer.purge();
        }
        isRunning = false;
        pause = false;
        event.setIsLastPosition(true);
        fireEventListeners();
    }
    
    public void pause() {
        getIO().getOut().println("[PAUSE]");
        pause = true;
    }
    
    private void makeTrajectory() {
        trajectory.clear();
        Position tmp = positions.get(0);
        for(int i=1; i<positions.size(); i++) {
            if(allowInterpolation) {
                trajectory.addAll(getInterpolatedPositions(tmp, positions.get(i)));
            } else {
                trajectory.add(tmp);
            }
            tmp = positions.get(i);
        }
    }
    
    private List<Position> getInterpolatedPositions(Position a, Position b) {
        
        List<Position> interpolatedPositions = new ArrayList<>();
        Position interpolatedPosition;
        
        for(double i=0d; i<1d; i+= 0.01) {
            
            switch(interpolationType) {
                
                case INTERPOLATION_RHUMB : 
                    interpolatedPosition = Position.interpolateRhumb(i, a, b); break;
                    
                case INTERPOLATION_GREAT_CIRCLE : 
                    interpolatedPosition = Position.interpolateGreatCircle(i, a, b); break;
                    
                case INTERPOLATION_LINEAR : 
                default : 
                    interpolatedPosition = Position.interpolate(i, a, b); break;
            }
            interpolatedPositions.add(interpolatedPosition);
        }
       
        return interpolatedPositions;
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="Events">
    @Override
    public void addEventListener(SimulatorEventListener listener) {
        positionListeners.add(listener);
    }
    
    @Override
    public void removeEventListener(SimulatorEventListener listener) {
        positionListeners.remove(listener);
    }
    
    @Override
    public void fireEventListeners() {
        for(SimulatorEventListener eventProvider : positionListeners) {
            eventProvider.updatePosition(event);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public int getPeriod() {
        return period;
    }
    
    public void setPeriod(int period) {
        
        if(period < 1) {
            throw new IllegalArgumentException("Non-positive or null period");
        }
        
        if(isRunning) {
            if(timer != null) {
                task.cancel();
            }
            
            timer = new Timer();
            task = new RemindTask();
            timer.schedule(task, 0, period);
        }
        this.period = period;
    }
    
    public List<Position> getPositions() {
        return positions;
    }
    
    public void setPositions(Collection<? extends Position> positions) {
        this.positions.clear();
        this.positions.addAll(positions);
    }
    
    public void addPosition(Position pos) {
        positions.add(pos);
    }
    
    public void clearPositions() {
        positions.clear();
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public boolean isPause() {
        return pause;
    }
    
    public int getInterpolationType() {
        return interpolationType;
    }

    public void setInterpolationType(int interpolationType) {
        this.interpolationType = interpolationType;
    }

    public boolean isAllowInterpolation() {
        return allowInterpolation;
    }

    public void setAllowInterpolation(boolean allowInterpolation) {
        this.allowInterpolation = allowInterpolation;
    }
    
    public static InputOutput getIO() {
        if (IO == null) {
            IO = IOProvider.getDefault().getIO("Simulator", true);
        }
        return IO;
    }
    //</editor-fold>
    
    public Track getTrack() {
        
        Track track = new GpxTrack("", "", "", null);
        GpxTrackSegment segment = new GpxTrackSegment("", "", "", null);
        List<TrackPoint> trackPoints = new ArrayList<>();
        List<TrackSegment> trackSegments = new ArrayList<>();
        
        if(trajectory.size() < 2) {
            makeTrajectory();
        }
        
        for(Position p : trajectory) {
            trackPoints.add(new TrackPointImpl(p));
        }
        
        segment.getPoints().addAll(trackPoints);
        trackSegments.add(segment);
        track.getSegments().addAll(trackSegments);

        return track;
    }
}
