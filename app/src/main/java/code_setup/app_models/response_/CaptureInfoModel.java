package code_setup.app_models.response_;

/**
 * Created by Electrovese on 6/13/2017.
 */

public class CaptureInfoModel {

    private String device_id;
    private String device_os;
    private String os_version;
    private String app_version;
    private String gcm_id;

    public CaptureInfoModel(String device_id,String device_os,String os_version,String app_version,String gcm_id){
        this.device_id=device_id;
        this.device_os=device_os;
        this.os_version=os_version;
        this.app_version=app_version;
        this.gcm_id=gcm_id;
    }
}
