
using Data.Repository;
using Data.Utility;
using Model;
using System;
using System.Collections.Generic;
using System.Data.Entity.Core.Objects;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Data.Services
{
    public class Common_Repository : ICommon_Repository
    {

        private dataEntities context;

        public Common_Repository(dataEntities context)
        {
            context.Configuration.ProxyCreationEnabled = false;
            this.context = context;
        }
        #region UserRelated

        public teacher Login_User(string userNm = "")
        {
            return context.teachers.Where(x => x.teachermobile == userNm || x.teacheremail == userNm).FirstOrDefault();

        }
        public Get_User_Login_Result Login_User(string userNm = "", string pass = "")
        {
            return context.Get_User_Login(userNm, pass).FirstOrDefault();
        }

        public int SaveUpdate_Password(int User_Id, string CurrenPassword, string NewPassword)
        {
            return Convert.ToInt16(context.UpdateUserPassword(User_Id, CurrenPassword, NewPassword).FirstOrDefault());
        }


        public teacher Get_ResetPassword_Details(string username)
        {
            return context.teachers.Where(x => x.teacheremail == username || x.teachermobile == username).FirstOrDefault();
        }

        public List<Get_Transactions_List_Result> gettransactionlog(string sSearch = "")
        {
            return context.Get_Transactions_List(sSearch).ToList();
        }

        public int SaveUpdate_ResetPassword(int User_Id, string NewPassword)
        {
            int returnValue = 0;
            var Existteacher = context.teachers.Where(x => x.teacherid == User_Id).FirstOrDefault();
            if (Existteacher != null)
            {
                Existteacher.password = NewPassword;
                context.Entry(Existteacher).CurrentValues.SetValues(Existteacher);
                context.SaveChanges();
                returnValue = 1;
            }
            return returnValue;
        }

        public int SaveUpdate_Delete_activitylogs(activitylog al)
        {
            int ResponseId = 0;
            try
            {
                context.activitylogs.Add(al);
                int i = context.SaveChanges();
                ResponseId = 1;
            }
            catch (Exception)
            {

                ResponseId = -1;
            }

            return ResponseId;
        }

        public Get_Transactions_List_Result SaveUpdate_Delete_transactionlog(string xmlString, bool IsDelete, out string OutIdentity)
        {

            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.transactionlog_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();

            if (Return_Value != 3)
            {
                return context.Get_Transactions_List(" and ts.transid=" + Return_Value).FirstOrDefault();
            }
            else
            {
                return null;
            }


        }


        public int SaveUpdate_Delete_smslogs(smslog sl)
        {
            int ResponseId = 0;
            try
            {
                context.smslogs.Add(sl);
                int i = context.SaveChanges();
                ResponseId = 1;
            }
            catch (Exception)
            {

                ResponseId = -1;
            }

            return ResponseId;
        }




        public List<Get_teacher_class_Result> gettecherclass(int teacherid)
        {
            return context.Get_teacher_class(teacherid).ToList();
        }
        public List<Get_teacher_subject_Result> gettechersubject(int teacherid)
        {
            return context.Get_teacher_subject(teacherid).ToList();
        }



        public bool userapikey(string Key)
        {
            try
            {
                var existkey = context.applicenses.Where(x => x.apikey == new Guid(Key)).FirstOrDefault();

                if (existkey != null)
                {
                    return true;
                }
                else
                {
                    return false;
                }

            }
            catch (Exception)
            {

                return false;
            }
        }
        public bool userapikey(string Key, string username, string password)
        {
            try
            {
                var exisUser = context.teachers.Where(x => (x.teachermobile == username || x.teacheremail == username) && x.password == password).FirstOrDefault();
                var existkey = context.applicenses.Where(x => x.apikey == new Guid(Key)).FirstOrDefault();

                if (existkey != null && exisUser != null)
                {
                    return true;
                }
                else
                {
                    return false;
                }

            }
            catch (Exception)
            {

                return false;
            }
        }

        public List<userrole> getuserrole()
        {
            return context.userroles.ToList();
        }

        #endregion UserRelated

        private bool disposed = false;
        private dataEntities dataEntities;

        protected virtual void Dispose(bool disposing)
        {
            if (!this.disposed)
            {
                if (disposing)
                {
                    context.Dispose();
                }
            }
            this.disposed = true;
        }

        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }
    }
}
