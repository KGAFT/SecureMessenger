
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface IOutputFile {

    @Multipart
    @POST("/uploadFile")
    Call uploadAttachment(@Part RequestBody fileDescription, @Part MultipartBody.Part file, @Query("appId") String appId, @Query("fileName")String fileName);
}
