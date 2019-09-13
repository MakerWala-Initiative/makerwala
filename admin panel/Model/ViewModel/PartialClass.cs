using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Model
{

    //public class Response
    //{
    //    public int Response { get; set; }
    //    public string status { get; set; }
    //    public string data { get; set; }
    //    public string message { get; set; }
    //}

    public class ApiResponse
    {
        public int status = 0;
        public string response = "failure";
        public string message;
        public object data;

        public ApiResponse(int Isstatus, string Message, object ResponseData, string ResponseId)
        {
            response = ResponseId;
            status = Isstatus;
            message = Message;
            data = ResponseData;
        }
    }

    public partial class transactionlog
    {
        public string Key { get; set; }
        public string username { get; set; }
        public string password { get; set; }
        public bool isrecorddelete { get; set; }
    }
    public partial class activitylog
    {
        public string Key { get; set; }
        public string username { get; set; }
        public string password { get; set; }
    }



    public partial class subject
    {
        public string classid { get; set; }
    }


    public partial class apiparameters
    {
        public string Key { get; set; }
        public List<int> classid { get; set; }
        public List<int> subjectid { get; set; }
        public int videoid { get; set; }

        public string username { get; set; }
        public string password { get; set; }
        public int type { get; set; }

        public int userid { get; set; }
        public string currentpassword { get; set; }
        public string newpassword { get; set; }

        public int countryid { get; set; }
        public int stateid { get; set; }
        public int pageindex { get; set; }


    }
    public class classids
    {
        public int classid { get; set; }
    }

    #region ApiClass


    public partial class login
    {

        public Get_User_Login_Result logindetail { get; set; }
        public List<Get_teacher_class_Result> teacherclass { get; set; }
        public List<Get_teacher_subject_Result> teachersubject { get; set; }


    }

    public partial class VideoList
    {
        public int totalrecords { get; set; }
        public List<Get_UploadVideo_List_Result> VideoLists { get; set; }

    }
    public partial class VideoDetails
    {
        public Get_UploadVideo_List_Result Details { get; set; }
        public List<Get_UploadVideo_List_Result> RelativeVideoList { get; set; }
        public List<Get_Transactions_List_Result> transactionlog { get; set; }
    }
    #endregion Apiclass



    #region TempClass




    public partial class Get_Teacher_List_Result
    {
        public string Key { get; set; }
        public List<int> classids { get; set; }
        public List<int> subjectids { get; set; }
        public string username { get; set; }
    }
    public partial class tteacher
    {

        public string Key { get; set; }
        public string classid { get; set; }
        public string subjectid { get; set; }

        public int teacherid { get; set; }
        public int blockid { get; set; }
        public string teacherfirstname { get; set; }
        public string teacherlastname { get; set; }
        public string teachermobile { get; set; }
        public string teacheremail { get; set; }
        public string teacheraddress { get; set; }
        public int countryid { get; set; }
        public int stateid { get; set; }
        public int cityid { get; set; }
        public string remarks { get; set; }
        public Nullable<bool> isblockadmin { get; set; }
        public string password { get; set; }
        public Nullable<int> userroleid { get; set; }
        public Nullable<bool> isactive { get; set; }
        public Nullable<int> schoolid { get; set; }
        public Nullable<int> createdby { get; set; }
        public Nullable<System.DateTime> createddate { get; set; }
        public Nullable<int> updatedby { get; set; }
        public Nullable<System.DateTime> updateddate { get; set; }
        public string gmailid { get; set; }
        public string imgprofile { get; set; }
        public string otpcode { get; set; }
        public Nullable<System.DateTime> optexpiredon { get; set; }

    }


    public partial class tcluster
    {
        public int clusterid { get; set; }
        public string clustername { get; set; }
        public string remarks { get; set; }
        public Nullable<bool> isactive { get; set; }
        public int createdby { get; set; }
        public System.DateTime createddate { get; set; }
        public Nullable<int> updatedby { get; set; }
        public Nullable<System.DateTime> updateddate { get; set; }
    }

    public partial class tclasslevel
    {
        public int classid { get; set; }
        public string classlevelname { get; set; }
        public int createdby { get; set; }
        public System.DateTime createddate { get; set; }
        public Nullable<int> updatedby { get; set; }
        public Nullable<System.DateTime> updateddate { get; set; }
        public string remarks { get; set; }
        public bool isactive { get; set; }
    }

    public partial class tcountry
    {
        public int countryid { get; set; }
        public string countryname { get; set; }
        public int createdby { get; set; }
        public System.DateTime createddate { get; set; }
        public Nullable<int> updatedby { get; set; }
        public Nullable<System.DateTime> updateddate { get; set; }
        public string remarks { get; set; }
        public Nullable<bool> isactive { get; set; }

    }

    public partial class ttype
    {
        public int typeid { get; set; }
        public string typename { get; set; }
        public string remarks { get; set; }
        public Nullable<bool> isactive { get; set; }
        public int createdby { get; set; }
        public System.DateTime createddate { get; set; }
        public Nullable<int> updatedby { get; set; }
        public Nullable<System.DateTime> updateddate { get; set; }

    }
    #endregion TempClass

}
