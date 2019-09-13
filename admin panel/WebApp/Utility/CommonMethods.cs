using Data.Repository;
using Data.Services;
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApp.Utility
{
    public static class CommonMethods
    {
        
        public static String CurrentDomain()
        {

            return System.Web.HttpContext.Current.Request.Url.GetLeftPart(UriPartial.Authority) + "/";
        }

        public static string DecryptString(string encrString)
        {
            byte[] b;
            string decrypted;
            try
            {
                b = Convert.FromBase64String(encrString);
                decrypted = System.Text.ASCIIEncoding.ASCII.GetString(b);
            }
            catch (FormatException fe)
            {
                decrypted = "";
            }
            return decrypted;
        }

        public static string EnryptString(string strEncrypted)
        {
            byte[] b = System.Text.ASCIIEncoding.ASCII.GetBytes(strEncrypted);
            string encrypted = Convert.ToBase64String(b);
            return encrypted;
        }
        public static void activitylogs(string Application = "", string Details = "", string Type = "", int UserId=0,string appversion="0.00")
        {
            ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());

            activitylog al = new activitylog();
            al.activitydate = DateTime.Now;
            al.application = Application;
            al.details = Details;
            al.type = Type;
            al.userid = UserId;
            al.appversion = System.Configuration.ConfigurationManager.AppSettings["webpages:Version"]; 
            _ICommon_Repository.SaveUpdate_Delete_activitylogs(al);
        }


        public static void Sendsms(string mobileno, string smstext)
        {
            // string smstext = "#OTPCODE# is your #APP# verification code";
            string strSMSTemplateURL = RequestHelpers.GetConfigValue("SMSTemplateURL").Replace("*","&");

            try
            {
                if (smstext == "")
                    return;

                string strMessageText = strSMSTemplateURL;

                strMessageText = strMessageText.Replace("#SMSTEXT#", smstext).ToString();
                strMessageText = strMessageText.Replace("#MOBILENO#", mobileno).ToString();

                if (!string.IsNullOrEmpty(strMessageText.ToString()))
                {
                    Uri uri = new Uri(strMessageText);
                    if ((uri.Scheme == System.Uri.UriSchemeHttp))
                    {
                        System.Net.HttpWebRequest httpRequest = (System.Net.HttpWebRequest)System.Net.HttpWebRequest.Create(uri);

                        httpRequest.ContentLength = 0;
                        httpRequest.ContentType = "application/x-www-form-urlencoded; charset=utf-8";
                        httpRequest.Method = "POST";
                        httpRequest.GetResponse();
                    }

                    //save into smslogs
                }
            }
            catch
            {
            }

        }


        public static string GetBase64StringForImage(string imgPath)
        {
            byte[] imageBytes = System.IO.File.ReadAllBytes(imgPath);
            string base64String = Convert.ToBase64String(imageBytes);
            return base64String;
        }

    }
}
