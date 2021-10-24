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

public class LZWEncoder {

	private static String File_Input = null;
	public static int Bit_Length;
	private static double MAX_TABLE_SIZE; //Max Table size is based on the bit length input.
	private static String LZWfilename;

	/**
	 * 计算LZW算法压缩比例
	 * @param fileStream
	 * @param byteArrLen 返回字节数组的长度，默认是2
	 * @return
	 */
	public static byte[] calcCompressedRatio(String fileStream, int byteArrLen) {
		Bit_Length = byteArrLen*8;
		double compressedRatio = Encode_string(fileStream, Bit_Length);
		// 二进制化
		byte[] ans = new byte[byteArrLen];
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
	 * @throws IOException */

	private static double Encode_string(String input_string, double Bit_Length) {

		MAX_TABLE_SIZE = Math.pow(2, Bit_Length);

		double table_Size =  255;

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
					TABLE.put(Str_Symbol, (int) table_Size++);
				initString = "" + symbol;
			}
		}

		if (!initString.equals(""))
			encoded_values.add(TABLE.get(initString));

		//CreateLZWfile(encoded_values);
		int compressedSize = encoded_values.size()*(int)Bit_Length;
		return compressedSize / (16.0 * input_string.length());
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

		StringBuffer input_string1 = new StringBuffer();

		try (BufferedReader br = Files.newBufferedReader(Paths.get(File_Input), StandardCharsets.UTF_8)) {
			for (String line = null; (line = br.readLine()) != null;) {

				input_string1 = input_string1.append(line);
			}
		}

		double compressedRatio = Encode_string(input_string1.toString(),Bit_Length);
		System.out.println(compressedRatio);
		// 二进制化
		int byteArrLen = 2;
		byte[] ans = new byte[byteArrLen];
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
		System.out.println(ans);
		for(int i = 0; i < byteArrLen; ++i) {
			System.out.println(ByteAndBitUtils.byte2String(ans[i]));
		}
	}
}
