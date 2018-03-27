
import algebra.*;

/**
 * The Renderer class drives the rendering pipeline: read in a scene, projects
 * the vertices and rasterizes every faces / edges.
 * @author: cdehais
 */
public class Renderer {

    static Scene scene;
    static Mesh mesh;
    static Rasterizer rasterizer;
    static GraphicsWrapper screen;
    static Shader shader;
    static Transformation xform;

    static void init (String sceneFilename) throws Exception {
        scene = new Scene (sceneFilename);
        mesh = new Mesh (scene.getMeshFileName());
        screen = new GraphicsWrapper (scene.getScreenW (), scene.getScreenH ());
        screen.clearBuffer ();
        //shader = new SimpleShader (screen);
        shader = new PainterShader (screen);

        rasterizer = new Rasterizer (shader);

        xform = new Transformation ();
        xform.setLookAt (scene.getCameraPosition (),
                         scene.getCameraLookAt (),
                         scene.getCameraUp ());
	xform.setProjection ();
        xform.setCalibration (scene.getCameraFocal (), scene.getScreenW (), scene.getScreenH ());
    }

    static Fragment[] projectVertices () {
        Vector[] vertices = mesh.getVertices ();
        Vector3[] normals = mesh.getNormals ();
        double[] colors = mesh.getColors ();

        Fragment[] fragments = new Fragment[vertices.length];

        try {
        for (int i = 0; i < vertices.length; i++) {
            Vector pVertex = xform.projectPoint (vertices[i]);
            Vector3 pNormal = normals[i];

            int x = (int) Math.round (pVertex.get (0));
            int y = (int) Math.round (pVertex.get (1));
            fragments[i] = new Fragment (x, y);
            fragments[i].setDepth (pVertex.get (2));
            fragments[i].setNormal (pNormal);
            fragments[i].setColor (colors[3*i], colors[3*i+1], colors[3*i+2]);
        }
        } catch (SizeMismatchException e) {
            e.printStackTrace ();
            /* should not reach */
        } catch (InstantiationException e) {
            e.printStackTrace ();
            /* should not reach */
        }

        return fragments;
    }

    static void renderWireframe () {
        Fragment[] fragment = projectVertices ();
        int[] faces = mesh.getFaces ();

        for (int i = 0; i < 3 * mesh.getNumFaces(); i += 3) {
            for (int j = 0; j < 3; j++) {
                Fragment v1 = fragment[faces[i + j]];
                Fragment v2 = fragment[faces[i + ((j+1) % 3) ]];
                rasterizer.rasterizeEdge (v1, v2);
            }
        }
    }

    static void renderSolid () {
        Fragment[] fragments = projectVertices ();
        int[] faces = mesh.getFaces ();

        for (int i = 0; i < 3 * mesh.getNumFaces(); i += 3) {
            Fragment v1 = fragments[faces[i]];
            Fragment v2 = fragments[faces[i+1]];
            Fragment v3 = fragments[faces[i+2]];

            rasterizer.rasterizeFace (v1, v2, v3);
        }
    }

    public static void wait (int sec) {
        try {
            Thread.sleep (sec * 1000);
        } catch (Exception e) {
            /* nothing */
        }
    }
    public static void main (String[] args) {

        if (args.length == 0) {
            System.out.println("usage: java Renderer <scene_file>");
        } else {
            try {
                init (args[0]); 
            } catch (Exception e) {
                System.out.println ("Problem initializing Renderer: " + e);
                e.printStackTrace ();
                return;
            }
        }

        /* wireframe rendering */
        renderWireframe ();
        screen.swapBuffers ();
        wait (3);

        /* solid rendering, no lighting */
        screen.clearBuffer ();
        shader.reset ();
        renderSolid ();
        screen.swapBuffers ();
        wait (3);

        screen.destroy ();
  	System.exit (0);
    }
}
