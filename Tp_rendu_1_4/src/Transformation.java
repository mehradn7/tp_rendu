
import algebra.*;

/**
 * author: cdehais
 */
public class Transformation {

	Matrix worldToCamera;
	Matrix projection;
	Matrix calibration;

	public Transformation() {
		try {
			worldToCamera = new Matrix("W2C", 4, 4);
			projection = new Matrix("P", 3, 4);
			calibration = Matrix.createIdentity(3);
			calibration.setName("K");
		} catch (InstantiationException e) {
			/* should not reach */
		}
	}

	public void setLookAt(Vector3 cam, Vector3 lookAt, Vector3 up) {
		try {
			// compute rotation

			/* Zcam */
			Vector3 zcam = new Vector3();
			double []coordonnees = {lookAt.get(0) - cam.get(0), lookAt.get(1)- cam.get(1), lookAt.get(2)- cam.get(2)};
			zcam.set(coordonnees);
			zcam.normalize();
			
			/* Xcam */
			Vector3 xcam = up.cross(zcam);
			xcam.normalize();
			
			
			/* Ycam */
			Vector3 ycam = zcam.cross(xcam);
			ycam.normalize();
			
			for (int i=0;i<=2;i++){
				worldToCamera.set(0, i, xcam.get(i));
				worldToCamera.set(1, i, ycam.get(i));
				worldToCamera.set(2, i, zcam.get(i));

			}
			

			
			// compute translation

			Matrix N_t = worldToCamera.getSubMatrix(0, 0, 3, 3);
			Vector3 T = new Vector3(N_t.multiply(cam));
			T.scale(-1);
			
			worldToCamera.set(0, 3, T.getX());
			worldToCamera.set(1, 3, T.getY());
			worldToCamera.set(2, 3, T.getZ());
			
			worldToCamera.set(3, 0, 0);
			worldToCamera.set(3, 1, 0);
			worldToCamera.set(3, 2, 0);
			worldToCamera.set(3, 3, 1);



			/* à compléter */

		} catch (Exception e) {
			/* unreached */ }
		;

		System.out.println("Modelview matrix:\n" + worldToCamera);
	}

	public void setProjection() {
		
		for (int i=0; i<3; i++){
			for (int j=0; j<4; j++){
				this.projection.set(i, j, 0);
			}
		}
		
		this.projection.set(0, 0, 1);
		this.projection.set(1, 1, 1);
		this.projection.set(2, 2, 1);

		System.out.println("Projection matrix:\n" + projection);
		
	}

	public void setCalibration(double focal, double width, double height) {

		/* à compléter */
		calibration.set(0, 0, focal);
		calibration.set(1, 1, focal);
		calibration.set(2, 2, 1);
		calibration.set(0, 2, width/2);
		calibration.set(1, 2, height/2);

		System.out.println("Calibration matrix:\n" + calibration);
	}

	/**
	 * Projects the given homogeneous, 4 dimensional point onto the screen. The
	 * resulting Vector as its (x,y) coordinates in pixel, and its z coordinate
	 * is the depth of the point in the camera coordinate system.
	 */
	public Vector3 projectPoint(Vector p) throws SizeMismatchException, InstantiationException {
		Vector ps = new Vector(3);

		ps = this.projection.multiply(this.worldToCamera.multiply(p));
		
		double z = ps.get(2);
		ps.set(0, ps.get(0)/z);
		ps.set(1, ps.get(1)/z);
		ps.set(2, ps.get(2)/z);
		
		ps = this.calibration.multiply(ps);
		
		ps.set(2, z);

		return new Vector3(ps);
	}

	/**
	 * Transform a vector from world to camera coordinates.
	 */
	public Vector3 transformVector(Vector3 v) throws SizeMismatchException, InstantiationException {
		/* Doing nothing special here because there is no scaling */
		Matrix R = worldToCamera.getSubMatrix(0, 0, 3, 3);
		Vector tv = R.multiply(v);
		return new Vector3(tv);
	}

}
