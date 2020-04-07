import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomFileGenerator {

	Dimension dim;

	public void RandomAccessFile() {
		dim = Dimension.KB;
	}

	public void RandomAccessFile(Dimension dim) {
		this.dim = dim;
	}

	public static void generate(String path, Dimension dim, double randFileSize) {

		try {
			RandomAccessFile randomFile = new RandomAccessFile(path, "rw");
			randomFile.setLength((int) (randFileSize * dim.getSizeAsInt()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}