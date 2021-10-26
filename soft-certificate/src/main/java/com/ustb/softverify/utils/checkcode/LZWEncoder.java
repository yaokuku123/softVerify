package com.ustb.softverify.utils.checkcode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.ustb.softverify.utils.FileUtil;

public class LZWEncoder {

	private static String File_Input = null;
	public static int Bit_Length;
	private static int MAX_TABLE_SIZE; //Max Table size is based on the bit length input.
	private static String LZWfilename;
	private static int MAX_STRING_SIZE = 10*1024*1024;

	/**
	 * 计算LZW算法压缩比例，在上传文件小于10MB的情况下，内存占用约 1～5 MB 级别
	 * 文件流长度为n，则时间复杂度约为 O(n)
	 * @param fileStream 读取文件字节流转化的字符串，字符串中字符要求为1Byte存储
	 * @param byteArrLen 返回字节数组的长度，默认是2
	 * @return
	 */
	public static byte[] calcCompressedRatio(String fileStream, int byteArrLen) {
		Bit_Length = 8*byteArrLen;
		byte[] ans = new byte[byteArrLen];
		if(fileStream == null) return ans;
		if(fileStream.length() > MAX_STRING_SIZE) {
			fileStream = fileStream.substring(0, MAX_STRING_SIZE);
		}
		double compressedRatio = Encode_string(fileStream, Bit_Length);
		// 二进制化
		double base = 1;
		double approx = 0;
		for (int i = 0; i < byteArrLen*8; ++i) {
			if (compressedRatio > approx) {
				setBit(ans, i, 1);
				approx += base/2;
			} else {
				setBit(ans, Math.max(i-1,0), 0);
				approx -= base/2;
			}
			base /= 2;
		}
		return ans;
	}

	private static void setBit(byte[] arr, int idx, int bit) {
		int arrIdx = idx / 8;
		int bitIdx = idx % 8;
		arr[arrIdx] &= ~(1<<bitIdx);
		arr[arrIdx] |= (bit<<bitIdx);
	}

	/** Compress a string to a list of output symbols and then pass it for compress file creation.
	 * @param Bit_Length //Provided as user input.
	 * @param input_string //Filename that is used for encoding.
	 */

	private static double Encode_string(String input_string, int Bit_Length) {
		MAX_TABLE_SIZE = 1 << Bit_Length;
		int table_Size =  255;

		Map<String, Integer> TABLE = new HashMap<String, Integer>();

		for (int i = 0; i < 255 ; i++)
			TABLE.put("" + (char) i, i);

		String initString = "";

		List<Integer> encoded_values = new ArrayList<Integer>();

		for (char symbol : input_string.toCharArray()) {
			String Str_Symbol = initString + symbol;
			if (TABLE.containsKey(Str_Symbol))
				initString = Str_Symbol;
			else {
				encoded_values.add(TABLE.get(initString));

				if(table_Size < MAX_TABLE_SIZE)
					TABLE.put(Str_Symbol, table_Size++);
				initString = "" + symbol;
			}
		}

		if (!initString.equals(""))
			encoded_values.add(TABLE.get(initString));

		//CreateLZWfile(encoded_values);
		int compressedSize = encoded_values.size()*Bit_Length;
		return (double)compressedSize / (Bit_Length * input_string.length());
	}


/*
@param encoded_values , This hold the encoded text.
@throws IOException
*/

	private static void CreateLZWfile(List<Integer> encoded_values) throws IOException {

		BufferedWriter out = null;

		LZWfilename = File_Input.substring(0,File_Input.indexOf(".")) + ".lzw";

		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(LZWfilename),"UTF_16BE")); //The Charset UTF-16BE is used to write as 16-bit compressed file

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Iterator<Integer> Itr = encoded_values.iterator();
			while (Itr.hasNext()) {
				out.write(Itr.next());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		out.flush();
		out.close();
	}


	public static void main(String[] args) throws IOException {

		File_Input = args[0];
		Bit_Length = Integer.parseInt(args[1]);

//		StringBuffer input_string1 = new StringBuffer();
//
//		try (BufferedReader br = Files.newBufferedReader(Paths.get(File_Input), StandardCharsets.UTF_8)) {
//			for (String line = null; (line = br.readLine()) != null;) {
//
//				input_string1 = input_string1.append(line);
//			}
//		}
		// 二进制化
		int byteArrLen = 2;
		byte[] ans = LZWEncoder.calcCompressedRatio(FileUtil.read(File_Input), 2);
		for(int i = 0; i < byteArrLen; ++i) {
			System.out.println(ByteAndBitUtils.byte2String(ans[i]));
		}
	}
}
