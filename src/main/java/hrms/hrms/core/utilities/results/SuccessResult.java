package hrms.hrms.core.utilities.results;

public class SuccessResult extends Result{
    public SuccessResult(){
        // İşlem sonucu başarılı ancak mesaj vermiyor.
        super(true);
    }

    public SuccessResult(String message){
        super(true,message);
    }
}
