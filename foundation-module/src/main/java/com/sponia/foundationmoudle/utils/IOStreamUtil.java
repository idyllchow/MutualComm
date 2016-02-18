package com.sponia.foundationmoudle.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @packageName com.sponia.foundationmoudle.utils
 * @description 输入输出流
 * @date 15/9/8
 * @auther shibo
 */
public final class IOStreamUtil {

	/**
	 * 将输入流转换为字符串
	 * 
	 * @param is 要转换的输入流
	 * @return 转换之后的字符串
	 * @throws IOException
	 *             如果要转化的输入流为空将抛出异常
	 */
	public static String inputStreamToString(InputStream is) throws IOException {
		if (is == null) {
			throw new IOException("不能将空的输入流转换为字符串");
		}
		byte[] data = inputStreamTobyte(is);
		return data == null ? null : new String(data);
	}

	/**
	 * 将输入流转化成字节数组
	 * 
	 * @param is 要转化的输入流
	 * @return 转化之后的字节数组
	 */
	public static byte[] inputStreamTobyte(InputStream is) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		boolean b = inputStreamToOutput(is, bos);
		return b ? bos.toByteArray() : null;
	}

	/**
	 * 输入流转为输出流
	 * @param is 要转化的输入流
	 * @param os 转化后的输出流
	 * @return true表示成功，false表示失败
	 */
	public static boolean inputStreamToOutput(InputStream is, OutputStream os) {
		try {
			if (is != null && os != null) {
				byte[] b = new byte[1024 * 3];
				int i = 0;
				while ((i = is.read(b)) != -1) {
					os.write(b, 0, i);
				}
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(is != null) {
					is.close();
				}
				if(os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
}
