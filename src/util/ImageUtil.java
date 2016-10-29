package util;

import java.io.*;

/**
 * Created by wuming on 16/2/22.
 */
public class ImageUtil {

    /**
     * 将文件转成base64 字符串
     *
     * @param path 文件路径
     * @return *
     * @throws Exception
     */

    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new Base64().encode(buffer);

    }

    /**
     * fileData base64编码的图片字符串
     * @param fileData
     * @param filePath
     * @throws IOException
     */
    public static void uploadImage(String fileData, String filePath) throws IOException {
        byte[] fileBytes = Base64.decode(fileData);
        BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(fileBytes));
        String path = filePath.substring(0, filePath.lastIndexOf("/"));
        File saveDir = new File(path);// path1为存放的路径
        if (!saveDir.exists()) {// 如果不存在文件夹，则自动生成
            saveDir.mkdirs();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] buffer = new byte[1024];
        int byteread = 0;
        while ((byteread = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, byteread); // 文件写操作
        }
        bos.flush();
        bos.close();
        bis.close();
    }
}
