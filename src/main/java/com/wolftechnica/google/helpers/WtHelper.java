package com.wolftechnica.google.helpers;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.wolftechnica.google.exceptions.WtCloudException;
import com.wolftechnica.google.exceptions.WtCloudExceptionCodes;
import com.wolftechnica.google.service.core.GCSCoreService;

/**
 * Class {@link WtHelper} provides the various utility that can make your
 * integration easy.
 * 
 * @author Wolftechnica
 *
 */
public class WtHelper {

	private static final Logger LOG = Logger.getLogger(GCSCoreService.class.getName());

	private WtHelper() throws IllegalAccessException {
		throw new IllegalAccessException("Static helper.");
	}

	/**
	 * create a random string value that can be used for various purpose.
	 * 
	 * @param length
	 *            : is the length of the the string value you that you need to
	 *            generate
	 * @return random string
	 */
	public static String generateRandomString(int length) {
		StringBuilder builder = new StringBuilder();
		while (length-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws WtCloudException
	 */
	public static byte[] fileToByteArray(File file) throws WtCloudException {
		byte[] bytesArray = new byte[(int) file.length()];
		try (FileInputStream fis = new FileInputStream(file)) {
			fis.read(bytesArray);
		} catch (FileNotFoundException e) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_SINGLETON_FILE_NOT_EXCEPTION);
		} catch (IOException e) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_SINGLETON_IO_EXCEPTION);
		}
		return bytesArray;

	}

	/**
	 * @param file
	 *            : byte array of file that needs to be resized
	 * @param height
	 *            : actual height in which file will be resized
	 * @param width
	 *            : actual width in which file will be resized
	 * @param fileName
	 *            : generated file name
	 * @return byte of the generated file.
	 * @throws IOException
	 * @throws WtCloudException
	 * 
	 * 
	 *             <br>
	 *             75x75 - Gallery thumbnail (filename-small.jpg)<br>
	 *             150x150 - Gallery thumbnail retina (filename-smallx2.jpg)<br>
	 *             480x320 - Full image iPhone (filename-medium.jpg)<br>
	 *             1024x768 - Full image Retina (filename-large.jpg)<br>
	 * 
	 */
	public static byte[] resizeImage(byte[] file, int width, int height, String imageType) throws IOException {
		InputStream originalImage = new ByteArrayInputStream(file);
		BufferedImage bImageFromConvert = ImageIO.read(originalImage);
		BufferedImage resizedImage = getScaledInstance(bImageFromConvert, width, height,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
		return convertBufferedImageToBYte(resizedImage, imageType);

	}

	private static byte[] convertBufferedImageToBYte(BufferedImage originalImage, String imageType) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, imageType, baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}

	public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint,
			boolean higherQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = img;
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

}
