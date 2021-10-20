package com.ustb.softverify.utils;

import com.ustb.softverify.entity.dto.CompInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadTxt {
    /**
     * 读取txt文件的内容
     * @param filePath 想要读取的文件对象
     * @return 返回文件内容
     */
    public static boolean comp2txt(String filePath, List<CompInfo> compInfoList){
        List<String> strList = new ArrayList<String>();
        boolean flag = true;
        File file = new File(filePath);
        InputStreamReader read = null;
        BufferedReader reader = null;
        try {
            read = new InputStreamReader(new FileInputStream(file),"GBK");
            reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                strList.add(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        for(CompInfo comp : compInfoList) {
            for (String s : strList) {
                boolean flag1 = s.contains(comp.getOrgName());
                boolean flag2 = s.contains(comp.getFileSize().toString());
                comp.setFlag(flag1 && flag2);
                if (comp.isFlag()) {
                    break;
                }
            }
            flag = flag && comp.isFlag();
        }
        return flag;
    }

}
