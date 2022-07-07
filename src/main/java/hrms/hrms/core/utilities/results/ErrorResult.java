package hrms.hrms.core.utilities.results;

public class ErrorResult extends Result{
    public ErrorResult(){
        // İşlem sonucu başarısız ancak mesaj vermiyor.
        super(false);
    }

    public ErrorResult(String message){
        super(false,message);
    }
}
