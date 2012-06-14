/*
 * WaveFrontLoader.java
 *
 * Created on March 16, 2008, 8:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.java.joglutils.model.loader;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import net.java.joglutils.model.ModelLoadException;
import net.java.joglutils.model.ResourceRetriever;
import net.java.joglutils.model.geometry.Bounds;
import net.java.joglutils.model.geometry.Face;
import net.java.joglutils.model.geometry.Material;
import net.java.joglutils.model.geometry.Mesh;
import net.java.joglutils.model.geometry.Model;
import net.java.joglutils.model.geometry.TexCoord;
import net.java.joglutils.model.geometry.Vec4;

/**
 *
 * @author RodgersGB
 *
 * Jan 2011 - Significant modifications by Cride5 to allow loading of more complex .obj files
 * with grouped meshes and a range of materials.
 */
public class WaveFrontLoader implements iLoader{

	public static final String VERTEX_DATA = "v ";
	public static final String NORMAL_DATA = "vn ";
	public static final String TEXTURE_DATA = "vt ";
	public static final String FACE_DATA = "f ";
	public static final String SMOOTHING_GROUP = "s ";
	public static final String GROUP = "g ";
	public static final String OBJECT = "o ";
	public static final String COMMENT = "#";
	public static final String EMPTY = "";
	// the model
	private Model model = null;
	/** Bounds of the model */
	private Bounds bounds = new Bounds();
	/** Center of the model */
	private Vec4 center = new Vec4(0.0f, 0.0f, 0.0f);
	private String baseDir = null;
	// Reference to objects read during loading
	private List<Vec4> allVertices;
	private List<Vec4> allNormals;
	private List<TexCoord> allTexVerts;
	private List<Face> faces;
	private Mesh mesh;
	/** Creates a new instance of myWaveFrontLoader */
	public WaveFrontLoader(){
	}
	int numComments = 0;

	/**
	 * Loads data from the given file to construct a Model
	 *
	 * Assumes individual meshes are specified as an uninterupted
	 * series of 'f' lines, any interruption causes a new mesh to be created
	 */
	public Model load(String path) throws ModelLoadException{
		allVertices = new ArrayList<Vec4>();
		allNormals = new ArrayList<Vec4>();
		allTexVerts = new ArrayList<TexCoord>();
		faces = new LinkedList<Face>();
		mesh = new Mesh();
		model = new Model(path);
		baseDir = "";
		String tokens[] = path.split("/");
		for(int i = 0; i < tokens.length - 1; i++){
			baseDir += tokens[i] + "/";
		}

		InputStream stream = null;
		try{
			stream = ResourceRetriever.getResourceAsInputStream(model.getSource());
			if(stream == null){
				throw new ModelLoadException("Stream is null");
			}
		}catch(IOException e){
			throw new ModelLoadException("Caught IO exception: " + e);
		}

		try{
			// Open a file handle and read the models data
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line = null;
			String nextLine = null;
			boolean buildingMesh = false;
			while(line != null || (line = br.readLine()) != null){
//System.out.println(line + " = " + mesh);
				// If we're building a mesh and we encounter a line not
				// specifiying a face, then this mesh definition is complete
				if(buildingMesh && !lineIs(FACE_DATA, line)){
					addMeshToModel();
					buildingMesh = false;
				}

				if(lineIs(COMMENT, line)){
					// ignore comments
					numComments++;
				}else if(lineIs(VERTEX_DATA, line)){
					Vec4 point = parsePoint(line);
					// Calculate the bounds for the entire model
					allVertices.add(point);
					bounds.calc(point);
				}else if(lineIs(NORMAL_DATA, line)){
					allNormals.add(parsePoint(line));
				}else if(lineIs(TEXTURE_DATA, line)){
					String[] s = line.split("\\s+");
					TexCoord tex = new TexCoord();
					tex.u = Float.parseFloat(s[1]);
					tex.v = Float.parseFloat(s[2]);
					allTexVerts.add(tex);
				}else if(lineIs(FACE_DATA, line)){
					faces.add(parseFace(line));
					buildingMesh = true;
				}else if(lineIs("mtllib ", line)){
					processMaterialLib(line);
				}else if(lineIs("usemtl ", line)){
					setMeshMaterial(line);
				}else if(lineIs(GROUP, line)){
					mesh.name = parseName(line);
				}else if(lineIs(OBJECT, line)){
//     System.out.println("Line is object:" + line);
				}
				// Use nextLine next if its been set
				line = nextLine == null ? null : nextLine;
				nextLine = null;
			}
		}catch(IOException e){
			throw new ModelLoadException("Failed to find or read OBJ: " + stream);
		}
		// Add last mesh
		addMeshToModel();

		// Calculate the center of the model
		center.x = 0.5f * (bounds.max.x + bounds.min.x);
		center.y = 0.5f * (bounds.max.y + bounds.min.y);
		center.z = 0.5f * (bounds.max.z + bounds.min.z);

		model.setBounds(this.bounds);
		model.setCenterPoint(this.center);

		// Set references to all vertice types
		Vec4[] v = new Vec4[allVertices.size()];
		allVertices.toArray(v);
		Vec4[] vn = new Vec4[allNormals.size()];
		allNormals.toArray(vn);
		TexCoord[] vt = new TexCoord[allTexVerts.size()];
		allTexVerts.toArray(vt);
		for(int i = 0; i < model.getNumberOfMeshes(); i++){
			Mesh m = model.getMesh(i);
			m.vertices = v;
			m.normals = vn;
			m.texCoords = vt;
		}

	// Translate all vertices to center the model
		for(int i = 0; i < v.length; i++){
			v[i].x -= center.x;
			v[i].y -= center.y;
			v[i].z -= center.z;
		}

//System.out.println(this.bounds.toString());
//System.out.println("meshes:" + model.getNumberOfMeshes());
//for(int i = 0; i < model.getNumberOfMeshes(); i++){
//System.out.println(model.getMesh(i));
//}



		return model;
	}



	// Add current mesh to model
	private void addMeshToModel(){
		// Only add mesh if faces have been loaded
		if(faces.size() > 0){
			// Add faces to mesh
			mesh.faces = new Face[faces.size()];
			faces.toArray(mesh.faces);
			model.addMesh(mesh);
			faces = new LinkedList<Face>();
		}
		// Start new mesh
		mesh = new Mesh();
	}

	private boolean lineIs(String type, String line){
		return line.startsWith(type);
	}

	private Face parseFace(String line){
		String s[] = line.split("\\s+");
		if(line.contains("//")){ // Pattern is present if obj has no texture
			for(int loop = 1; loop < s.length; loop++){
				s[loop] = s[loop].replaceAll("//", "/-1/"); //insert -1 for missing vt data
			}
		}

		Face face = new Face(s.length - 1);

		for(int i = 1; i < s.length; i++){
			String s1 = s[i];
			String[] temp = s1.split("/");

			if(temp.length > 0){ // we have vertex data
				int idx = Integer.valueOf(temp[0]);
				if(idx < 0){ // Relative index
					face.vertIndex[i - 1] = allVertices.size() - idx;
				}else{ // Absolute index
					face.vertIndex[i - 1] = idx - 1;
				}
			}

			if(temp.length > 1){ // we have texture data
				int idx = Integer.valueOf(temp[1]);
				if(idx < 0){
					face.coordIndex[i - 1] = allTexVerts.size() - idx;
				}else{
					face.coordIndex[i - 1] = idx - 1;
				}
			}

			if(temp.length > 2){ // we have normal data
				int idx = Integer.valueOf(temp[2]);
				if(idx < 0){
					face.normalIndex[i - 1] =  allNormals.size() - idx;
				}else{
					face.normalIndex[i - 1] = idx - 1;
				}
			}
		}

		return face;
	}

	private Vec4 parsePoint(String line){
		Vec4 point = new Vec4();

		final String s[] = line.split("\\s+");

		point.x = Float.parseFloat(s[1]);
		point.y = Float.parseFloat(s[2]);
		point.z = Float.parseFloat(s[3]);

		return point;
	}

	private String parseName(String line){
		String name;

		final String s[] = line.split("\\s+");

		name = s[1];

		return name;
	}

	private void processMaterialLib(String mtlData){
		String s[] = mtlData.split("\\s+");

		InputStream stream = null;
		try{
			stream = ResourceRetriever.getResourceAsInputStream(baseDir + s[1]);
		}catch(IOException ex){
			ex.printStackTrace();
		}


		if(stream == null){
			try{
				stream = new FileInputStream(baseDir + s[1]);
			}catch(FileNotFoundException ex){
				ex.printStackTrace();
				return;
			}
		}
		reatMaterialFile(stream);
	}

	private void setMeshMaterial(String line){
		String s[] = line.split("\\s+");

		int materialID = -1;

		for(int i = 0; i < model.getNumberOfMaterials(); i++){
			Material mat = model.getMaterial(i);


			if(mat.strName.equals(s[1])){
				materialID = i;
				if(mat.strFile != null){
					mesh.hasTexture = true;
				}else{
					mesh.hasTexture = false;
				}
				break;
			}
		}
		if(materialID != -1){
			mesh.materialID = materialID;
		}

	}

	public void reatMaterialFile(InputStream stream){
		Material mat = null;
		int texId = 0;

		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line;

			while((line = br.readLine()) != null){

				String parts[] = line.trim().split("\\s+");

				if(parts[0].equals("newmtl")){
					if(mat != null){
						model.addMaterial(mat);
					}

					mat = new Material();
					mat.strName = parts[1];
					mat.textureId = texId++;

				}else if(parts[0].equals("Ks")){
					mat.specularColor = parseColor(line);
				}else if(parts[0].equals("Ns")){
					if(parts.length > 1){
						mat.shininess = Float.valueOf(parts[1]);
					}
				}else if(parts[0].equals("d"));else if(parts[0].equals("illum"));else if(parts[0].equals("Ka")){
					mat.ambientColor = parseColor(line);
				}else if(parts[0].equals("Kd")){
					mat.diffuseColor = parseColor(line);
				}else if(parts[0].equals("map_Kd")){
					if(parts.length > 1){
						mat.strFile = /*baseDir + */ parts[1];
					}
				}else if(parts[0].equals("map_Ka")){
					if(parts.length > 1){
						mat.strFile = /*baseDir + */ parts[1];
					}
				}
			}

			br.close();
			model.addMaterial(mat);

		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	private Color parseColor(String line){
		String parts[] = line.trim().split("\\s+");

		Color color = new Color(Float.valueOf(parts[1]),
				  Float.valueOf(parts[2]), Float.valueOf(parts[3]));

		return color;
	}

	public static void main(String[] args){
		WaveFrontLoader loader = new WaveFrontLoader();
		try{
			loader.load("C:\\Documents and Settings\\RodgersGB\\My Documents\\Projects\\JOGLUTILS\\src\\net\\java\\joglutils\\examples\\models\\obj\\penguin.obj");
		}catch(ModelLoadException ex){
			ex.printStackTrace();
		}
	}
}
