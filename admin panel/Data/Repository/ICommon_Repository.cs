
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Data.Repository
{
    public interface ICommon_Repository : IDisposable
    {

        #region UserRelated
        teacher Login_User(string userNm = "");
        Get_User_Login_Result Login_User(string userNm = "", string pass = "");
        int SaveUpdate_Password(int User_Id, string CurrenPassword, string NewPassword);
        int SaveUpdate_ResetPassword(int User_Id, string NewPassword);
        teacher Get_ResetPassword_Details(string username);
        List<Get_Transactions_List_Result> gettransactionlog(string sSearch = "");
        int SaveUpdate_Delete_activitylogs(activitylog al);
        Get_Transactions_List_Result SaveUpdate_Delete_transactionlog(string xmlString, bool IsDelete, out string OutIdentity);
        int SaveUpdate_Delete_smslogs(smslog sl);
        List<Get_teacher_subject_Result> gettechersubject(int teacherid);
        List<Get_teacher_class_Result> gettecherclass(int teacherid);
        bool userapikey(string Key);
        bool userapikey(string Key, string username, string password);
        List<userrole> getuserrole();
        #endregion UserRelated

    }
}
