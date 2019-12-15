package utility;

/**
 * Created by admin on 2019/11/27.
 */
public class Test {
    public CErrors mErrors = new CErrors(); // 错误信息

    public  void test(){
        CError tError = new CError();
        tError.moduleName = "FileDeal";
        tError.functionName = "FileCopy";
        tError.errorMessage = "文件复制出错!";
        this.mErrors.addOneError(tError);
    }
    public static void main(String[] args) {
      Test t= new Test();
      t.test();
        System.out.println(t.mErrors.getErrContent());
    }
}
