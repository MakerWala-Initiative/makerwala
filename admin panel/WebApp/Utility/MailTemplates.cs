using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;

namespace WebApp.Utility
{
    public class MailTemplates
    {

      

        public static int ResetMail(teacher teacher)
        {
            string WebsiteName = CommonMethods.CurrentDomain();

            StringBuilder sb = new StringBuilder();
            sb.Append("<tr>");
            sb.Append("<td class='mobile-padding' align='center' style='padding: 0 15px 0 15px;'>");

            sb.Append("<table bgcolor='#ffffff' cellpadding='0' cellspacing='0' width='100%' style='max-width: 700px; border: 1px solid #ced2d8;'>");
            sb.Append("<tbody><tr>");
            sb.Append("<td height='40'></td>");
            sb.Append("</tr>");
            sb.Append("<tr>");
            sb.Append("<td align='center' style='padding: 0 15px 0 15px'>");

            sb.Append("<table width='100%' border='0' cellspacing='0' cellpadding='0' style='max-width: 600px; margin: 0; font-size: 18px; line-height: 34px; color: #464646; text-align: left;'>");
            sb.Append("<tbody><tr>");
            sb.Append("<td align='center' style='line-height:40px;font-size: 25px'>Reset your password </td>");
            sb.Append("</tr>");
            sb.Append("<tr>");
            sb.Append("<td height='20'></td>");
            sb.Append("</tr>");
            sb.Append("<tr>");
            sb.Append("<td>");
            sb.Append("Thank you for using MakerWala. You've entered " + teacher.teacheremail + " as the email address for your MakerWala account. Please Reset password");
            sb.Append(" Using click the button below:");
            sb.Append("</td>");
            sb.Append("</tr>");
            sb.Append("<tr>");
            sb.Append("<td height='40'></td>");
            sb.Append("</tr>");
            sb.Append("<tr>");
            sb.Append("<td align='center'>");
            sb.Append("Your username: <span style='font-weight: 700'>" + teacher.teacheremail +  "</span>");
            sb.Append("</td>");
            sb.Append("</tr>");
            sb.Append("<tr>");
            sb.Append("<td height='30'></td>");
            sb.Append("</tr>");
            sb.Append("<tr>");
            sb.Append("<td align='center'>");
            sb.Append("<table class='cta-button' width='100%' align='center' border='0' cellspacing='0' cellpadding='0' style='font-size: 16px; line-height: 30px; font-weight: 300; text-align: center; max-width: 400px;'>");
            sb.Append("<tbody><tr>");
            sb.Append("<td height='50' bgcolor='#2e59d9' style='border-radius: 5px;'>");
            sb.Append("<a href=" + WebsiteName + "/Admin/ResetPassword?id=" + CommonMethods.EnryptString(teacher.teacherid + "*" + DateTime.Now.ToString("dd-MMM-yyyy hh:mm:ss tt")) + " style='text-decoration: none;color: #ffffff; max-width: 100%; display: inline-block; font-weight: 700;padding: 10px 0' target='_blank'>Reset Now </a>");
            sb.Append("</td>");
            sb.Append("</tr>");
            sb.Append("</tbody></table>");
            sb.Append("</td>");
            sb.Append("</tr>");
            sb.Append("<tr>");
            sb.Append("<td height='30'></td>");
            sb.Append("</tr>");
            sb.Append("</tbody></table>");
            sb.Append("</td>");
            sb.Append("</tr>");
            sb.Append("</tbody></table>");
            sb.Append("</td>");
            sb.Append("</tr>");

            return SendMail.SendEmail(teacher.teachermobile + " " + teacher.teacheremail, teacher.teacheremail, sb.ToString(), "User Reset Password - MakerWala");

        }

    }
}