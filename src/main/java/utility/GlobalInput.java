package utility;

public class GlobalInput
{
    public String Operator;
    public String ManageCom;
    public String ComCode;
    public String LogNo;


    public GlobalInput()
    {
    }

    public void setSchema(GlobalInput cGlobalInput)
    {
        this.Operator = cGlobalInput.Operator;
        this.ComCode = cGlobalInput.ComCode;
        this.ManageCom = cGlobalInput.ManageCom;
        this.LogNo = cGlobalInput.LogNo;
    }
}
