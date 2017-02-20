package com.koolearn.cloud.util;

/**
 * Created by fn on 2016/4/19.
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CreateSimpleExcelToDisk {

    /**
     * @功能：手工构建一个简单格式的Excel
     */
    private static List<Student> getStudent() throws Exception
    {
        List list = new ArrayList();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");

        Student user1 = new Student(1, "张三", 16, df.parse("1997-03-12"));
        Student user2 = new Student(2, "李四", 17, df.parse("1996-08-12"));
        Student user3 = new Student(3, "王五", 26, df.parse("1985-11-12"));
        list.add(user1);
        list.add(user2);
        list.add(user3);

        return list;
    }

    public static void main(String[] args) throws Exception
    {

    }
}
