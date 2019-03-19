package Util;

import Service.Game;
import constant.FileConstant;
import jxl.Workbook;
import jxl.write.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    public static void createXl() throws IOException, WriteException {
        File file = new File(FileConstant.DATA_PATH);
        WritableWorkbook writableWorkbook;WritableSheet sheet;
        if(!file.exists()){
            writableWorkbook = Workbook.createWorkbook(file);
            writableWorkbook.createSheet("First Sheet", 0);
            writableWorkbook.write();
            writableWorkbook.close();
        }
    }
    public static void editXl(Game game){
        File file = new File(FileConstant.DATA_PATH);
        Workbook workbook = null;
        WritableWorkbook wtbook = null;
        WritableSheet wtSheet = null;
        try {
            workbook = Workbook.getWorkbook(file);
            // jxl.Workbook 对象是只读的，所以如果要修改Excel，需要创建一个可写的副本，副本指向原Excel文件（即下面的new File(excelpath)）
            wtbook = Workbook.createWorkbook(file, workbook);
            wtSheet = wtbook.getSheet(0);
            int rawNum = wtSheet.getRows();
            String[] data = getData(game);
            for(int i = 0; i < 6; i++){
                Label lbl = new Label(i, rawNum, data[i]);
                wtSheet.addCell(lbl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert wtbook != null;
                wtbook.write();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                wtbook.close();
            } catch (WriteException | IOException e) {
                e.printStackTrace();
            }
            workbook.close();
        }
    }
    private static String[] getData(Game game){
        String[] data = new String[6];
        data[0] = getTime();
        data[1] = "" + (game.getGameTime() / 1000);
        data[2] = "" + game.getDimension() + "*" + game.getDimension();
        data[3] = game.getPlayerColor()?"human":"computer";
        data[4] = !game.getPlayerColor()?"human":"computer";
        data[5] = game.checkScore();
        return data;
    }
    private static String getTime(){
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return dateFormat.format(date);
    }
}
