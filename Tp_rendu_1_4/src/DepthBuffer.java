
import java.lang.Double;

/**
 * The DepthBuffer class implements a DepthBuffer and its pass test.
 */
public class DepthBuffer {
	private double[] buffer;
	int width;
	int height;

	/**
	 * Constructs a DepthBuffer of size width x height. The buffer is initially
	 * cleared.
	 */
	public DepthBuffer(int width, int height) {
		buffer = new double[width * height];
		this.width = width;
		this.height = height;
		clear();
	}

	/**
	 * Clears the buffer to infinite depth for all fragments.
	 */
	public void clear() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				buffer[i * width + j] = Double.POSITIVE_INFINITY;
			}
		}

	}

	/**
	 * Test if a fragment passes the DepthBuffer test, i.e. is the fragment the
	 * closest at its position.
	 */
	public boolean testFragment(Fragment fragment) {
		if ((fragment.getX() >= 0) && (fragment.getX() < width) && (fragment.getY() >= 0)
				&& (fragment.getY() < height)) {

			/* compléter */

			return fragment.getAttribute(0) < this.buffer[fragment.getX() * width + fragment.getY()];
		} else {
			return false;
		}
	}

	/**
	 * Writes the fragment depth to the buffer
	 */
	public void writeFragment(Fragment fragment) {

		/* compléter */
		if (this.testFragment(fragment)) {
			this.buffer[fragment.getX() * width + fragment.getY()] = fragment.getAttribute(0);
			System.out.println("HAHA");
		}

	}

}
