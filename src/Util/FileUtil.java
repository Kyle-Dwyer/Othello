package Util;

import com.csvreader.CsvWriter;
import constant.FileConstant;
import constant.InfoConstant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;


public class FileUtil {
    private static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            return true;
        } else {
            if (destFileName.endsWith(File.separator)) {
                throw new RuntimeException(MessageFormat.format(InfoConstant.FILE_CANNOT_BE_DIR, destFileName));
            }
            // file exists?
            if (!file.getParentFile().exists()) {
                //if the parent dir is not exist, then create it.
                if (!file.getParentFile().mkdirs()) {
                    throw new RuntimeException(InfoConstant.FAILED_CREAT_DIR);
                }
            }
            // create target file.
            try {
                if (file.createNewFile()) {
                    return true;
                } else {
                    throw new RuntimeException(MessageFormat.format(InfoConstant.FAILED_TO_CREATE_FILE, destFileName));
                }
            } catch (IOException e) {
                throw new RuntimeException(MessageFormat.format(InfoConstant.FAILED_TO_CREATE_FILE_REASON, destFileName,
                        e.getMessage()));
            }
        }
    }

    public static void write(String[] array, String dataFilePath) {
        if (createFile(dataFilePath)) {
            BufferedWriter bw;
            try {
                bw = new BufferedWriter(new FileWriter(dataFilePath, true));
                CsvWriter out = new CsvWriter(bw, FileConstant.CSV_SEPARATOR);
                out.writeRecord(array);
                out.flush();
                bw.flush();
                out.close();
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

//    public static void createXl() throws IOException, WriteException {
//        File file = new File(FileConstant.DATA_PATH);
//        WritableWorkbook writableWorkbook;WritableSheet sheet;
//        if(!file.exists()){
//            writableWorkbook = Workbook.createWorkbook(file);
//            writableWorkbook.createSheet("First Sheet", 0);
//            writableWorkbook.write();
//            writableWorkbook.close();
//        }
//    }
//    public static void editXl(Game game){
//        File file = new File(FileConstant.DATA_PATH);
//        Workbook workbook = null;
//        WritableWorkbook wtbook = null;
//        WritableSheet wtSheet = null;
//        try {
//            workbook = Workbook.getWorkbook(file);
//            // jxl.Workbook 对象是只读的，所以如果要修改Excel，需要创建一个可写的副本，副本指向原Excel文件（即下面的new File(excelpath)）
//            wtbook = Workbook.createWorkbook(file, workbook);
//            wtSheet = wtbook.getSheet(0);
//            int rawNum = wtSheet.getRows();
//            String[] data = getData(game);
//            for(int i = 0; i < 6; i++){
//                Label lbl = new Label(i, rawNum, data[i]);
//                wtSheet.addCell(lbl);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                assert wtbook != null;
//                wtbook.write();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                wtbook.close();
//            } catch (WriteException | IOException e) {
//                e.printStackTrace();
//            }
//            workbook.close();
//        }
//    }
}
