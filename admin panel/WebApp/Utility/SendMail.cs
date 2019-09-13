using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Mail;
using System.Net.Security;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.Web;

namespace WebApp.Utility
{
    public class SendMail
    {

        private static string SetHeader()
        {
            string WebsiteName = RequestHelpers.GetConfigValue("Website");
            StringBuilder sbHeader = new StringBuilder();
            sbHeader.Append("<table class='email-container' bgcolor='#edf1f6' border='0' cellpadding='0' cellspacing='0' width='100%'>");

            sbHeader.Append("<tbody>");
            sbHeader.Append("<tr>");
            sbHeader.Append("<td align='center'>");

            sbHeader.Append("<table class='header' border='0' cellpadding='0' cellspacing='0' width='100%' style='max-width: 700px;'>");
            sbHeader.Append("<tbody><tr>");
            sbHeader.Append("<td style='text-align: left; padding: 15px 0;'>");
            sbHeader.Append(" <a href='" + WebsiteName + "' target='_blank'>");
            sbHeader.Append("<img width='176' align='center' style='border:none;' src='" + WebsiteName + "/Admin/Content/img/logo.jpg' alt='MakerWala Logo'>");
            sbHeader.Append("</a>");
            sbHeader.Append("</td>");
            sbHeader.Append("</tr></tbody></table></td></tr>");
            return Convert.ToString(sbHeader);
        }

        //  Set Mail Footer Function
        private static string SetFooter()
        {
            StringBuilder sbFooter = new StringBuilder();

            sbFooter.Append("<tr>");
            sbFooter.Append("<td class='mobile-padding' align='center' style='padding: 0 15px 0 15px'>");

            sbFooter.Append("<table border='0' cellpadding='0' cellspacing='0' width='100%' style='max-width: 700px; background-color: #edf1f6; line-height: 30px; margin: 0; font-size: 18px; color: #595959; text-align: center;'>");
            sbFooter.Append("<tbody>");
            sbFooter.Append("<tr>");
            sbFooter.Append("<td height='40'></td>");
            sbFooter.Append("</tr>");
            //sbFooter.Append("<tr>");
            //sbFooter.Append("<td>Need help with anything?<br> As always, our");
            ////sbFooter.Append("<a target='_blank' href='#' style='color: #439dff; font-weight: 600'>24/7 friendly support team</a> is here to help!");
            //sbFooter.Append("</td>");
            //sbFooter.Append("</tr>");
            sbFooter.Append("<tr>");
            sbFooter.Append("<td height='40'></td>");
            sbFooter.Append("</tr>");
            sbFooter.Append("<tr>");
            sbFooter.Append("<td>Warmest Regards,");
            sbFooter.Append("</td>");
            sbFooter.Append("</tr>");
            sbFooter.Append("<tr>");
            sbFooter.Append("<td style='line-height: 43px; margin: 0; font-size: 22px; font-weight: 600;'>The MakerWala Team");
            sbFooter.Append("</td>");
            sbFooter.Append("</tr>");
            sbFooter.Append("<tr>");
            sbFooter.Append("<td height='20'></td>");
            sbFooter.Append("</tr>");
            sbFooter.Append("<tr>");
            //sbFooter.Append("<td align='center'>");

            //sbFooter.Append("<table class='social-area' align='center' width='100%' cellpadding='0' cellspacing='0' border='0' style='max-width: 450px;'>");
            //sbFooter.Append("<tbody>");
            //sbFooter.Append("<tr>");
            //sbFooter.Append("<td class='social-area-td' width='50%'>");
            //sbFooter.Append("<table width='100%' border='0' cellspacing='0' cellpadding='0' style='text-align: center'>");
            //sbFooter.Append("<tbody>");
            //sbFooter.Append("<tr>");
            //sbFooter.Append("<td>");
            //sbFooter.Append("<a href='https://twitter.com/' target='_blank' style='display: inline-block;'>");
            //sbFooter.Append("<img width='43' style='border: none;' align='center' src='https://emails.jotform.com/img/common/tw_o.png' alt='MakerWala Footer Twitter'>");
            //sbFooter.Append("</a>");
            //sbFooter.Append("</td>");
            //sbFooter.Append("<td>");
            //sbFooter.Append("<a href='https://www.facebook.com/' target='_blank' style='display: inline-block;'>");
            //sbFooter.Append("<img width='43' style='border: none;' align='center' src='https://emails.jotform.com/img/common/fb_o.png' alt='MakerWala Footer Facebook'>");
            //sbFooter.Append("</a>");
            //sbFooter.Append("</td>");
            //sbFooter.Append(" <td>");
            //sbFooter.Append("<a href='https://www.linkedin.com/' target='_blank' style='display: inline-block;'>");
            //sbFooter.Append("<img width='43' style='border: none;' align='center' src='https://emails.jotform.com/img/common/in_o.png' alt='MakerWala Footer LinkedIn'>");
            //sbFooter.Append("</a>");
            //sbFooter.Append("</td>");

            //sbFooter.Append("</tr>");
            //sbFooter.Append("</tbody>");
            //sbFooter.Append("</table>");
            //sbFooter.Append("</td>");
            //sbFooter.Append("<td class='social-area-td' width='50%'>");
            //sbFooter.Append("<table width='100%' border='0' cellspacing='0' cellpadding='0' style='text-align: center'>");
            //sbFooter.Append("<tbody>");
            //sbFooter.Append(" <tr>");
            //sbFooter.Append(" <td>");
            //sbFooter.Append(" <a href='https://www.pinterest.com/' target='_blank' style='display: inline-block;'>");
            //sbFooter.Append("<img width='43' style='border: none;' align='center' src='https://emails.jotform.com/img/common/pin_o.png?v=0.1' alt='MakerWala Footer Pinterest'>");
            //sbFooter.Append("</a>");
            //sbFooter.Append("</td>");
            //sbFooter.Append("<td>");
            //sbFooter.Append("<a href='https://www.instagram.com/' target='_blank' style='display: inline-block;'>");
            //sbFooter.Append("<img width='43' style='border: none;' align='center' src='https://emails.jotform.com/img/common/ins_o.png?v=0.22' alt='MakerWala Footer Instagram'>");
            //sbFooter.Append("</a>");
            //sbFooter.Append("</td>");
            //sbFooter.Append("<td>");
            //sbFooter.Append("<a href='http://www.youtube.com/' target='_blank' style='display: inline-block;'>");
            //sbFooter.Append("<img width='43' style='border: none;' align='center' src='https://emails.jotform.com/img/common/youtube_o.png?v=0.1' alt='MakerWala Footer YouTube'>");
            //sbFooter.Append("</a>");
            //sbFooter.Append("</td>");

            //sbFooter.Append("</tr>");
            //sbFooter.Append("</tbody>");
            //sbFooter.Append("</table>");
            //sbFooter.Append("</td>");
            //sbFooter.Append("</tr>");
            //sbFooter.Append("</tbody>");
            //sbFooter.Append("</table>");
            //sbFooter.Append("</td>");
            //sbFooter.Append("</tr>");
            sbFooter.Append("<tr>");
            sbFooter.Append("<td height='20'></td>");
            sbFooter.Append("</tr>");
            sbFooter.Append("</tbody>");
            sbFooter.Append("</table>");
            sbFooter.Append("</td>");
            sbFooter.Append("</tr>");


            return Convert.ToString(sbFooter);

        }

        public static int SendEmail(string ReceiverName, string ReceiverEmail, string stHtmlBody, string subject)
        {
            int Response_Id = 0;
            try
            {

                StringBuilder sbHTML = new StringBuilder();
                // sbHTML.Append(SetClasses());
                sbHTML.Append(SetHeader());
                sbHTML.Append(stHtmlBody);
                sbHTML.Append(SetFooter());

                string username = RequestHelpers.GetConfigValue("FromUserName");
                string password = RequestHelpers.GetConfigValue("FromPassword");
                string Host = RequestHelpers.GetConfigValue("Host");
                string CompanySite = RequestHelpers.GetConfigValue("CompanySite");
                int Port = Convert.ToInt32(RequestHelpers.GetConfigValue("Port"));
                Boolean EnableSSL = Convert.ToBoolean(RequestHelpers.GetConfigValue("EnableSSL"));

                NetworkCredential loginInfo = new NetworkCredential(username, password);
                MailMessage msg = new MailMessage();

                SmtpClient smtpClient = new SmtpClient(Host, Port);
                smtpClient.EnableSsl = EnableSSL;
                smtpClient.UseDefaultCredentials = false;
                smtpClient.Credentials = loginInfo;

                #region Mail id attachament
                if (ReceiverEmail.Contains(","))
                {
                    string[] EmailIDs = ReceiverEmail.Split(',');
                    int i;
                    for (i = 0; i < EmailIDs.Length; i++)
                    {
                        if (EmailIDs[i].Contains("@") == true && EmailIDs[i].Contains(".") == true)
                        {
                            msg.To.Add(EmailIDs[i]);
                        }
                    }
                }
                else
                {
                    if (ReceiverEmail.Contains("@") == true && ReceiverEmail.Contains(".") == true)
                    {
                        msg.To.Add(ReceiverEmail);
                    }
                }
                var CCEmailAddress = System.Configuration.ConfigurationManager.AppSettings["CCMailID"];
                if (CCEmailAddress != "")
                {
                    if (CCEmailAddress.Contains("@") == true && CCEmailAddress.Contains(".") == true)
                    {
                        msg.CC.Add(CCEmailAddress);
                    }
                }
                #endregion Mail id attachament

                msg.From = new MailAddress(username, CompanySite);
                msg.To.Add(new MailAddress(ReceiverEmail));
                msg.Subject = subject;
                msg.Body = sbHTML.ToString();
                msg.IsBodyHtml = true;

                ServicePointManager.ServerCertificateValidationCallback = delegate(object s, X509Certificate certificate, X509Chain chain, SslPolicyErrors sslPolicyErrors) { return true; };
                smtpClient.Send(msg);
                Response_Id = 1;

            }
            catch (Exception ex)
            {

                Response_Id = -1;
            }
            return Response_Id;
        }

        public static string SendingMailOrderAttachmentPdf(string stHtmlBody, string stSubject, Stream stAttachmentFileName,
                                  string stAttachmentFileNamePdf, string stToEmailAddresses, string CCEmailAddress, bool isBCCRequired)
        {
            string stReturnText = string.Empty;

            StringBuilder sbHTML = new StringBuilder();
            sbHTML.Append(SetHeader());
            sbHTML.Append(stHtmlBody);
            sbHTML.Append(SetFooter());

            //Set SmtpClient to send Email
            string stFromUserName = RequestHelpers.GetConfigValue("FromUserName");
            string stFromPassword = RequestHelpers.GetConfigValue("FromPassword");
            int inPort = Convert.ToInt32(RequestHelpers.GetConfigValue("Port"));
            string stHost = RequestHelpers.GetConfigValue("Host");
            bool btIsSSL = Convert.ToBoolean(RequestHelpers.GetConfigValue("EnableSSL"));

            MailMessage objEmail = new MailMessage();
            MailAddress from = new MailAddress("\"" + RequestHelpers.GetConfigValue("Title") + "\" " + stFromUserName);
            objEmail.From = from;

            if (stToEmailAddresses.Contains(","))
            {
                string[] EmailIDs = stToEmailAddresses.Split(',');
                int i;
                for (i = 0; i < EmailIDs.Length; i++)
                {
                    if (EmailIDs[i].Contains("@") == true && EmailIDs[i].Contains(".") == true)
                    {
                        objEmail.To.Add(EmailIDs[i]);
                    }
                }
            }
            else
            {
                if (stToEmailAddresses.Contains("@") == true && stToEmailAddresses.Contains(".") == true)
                {
                    objEmail.To.Add(stToEmailAddresses);
                }
            }

            objEmail.Subject = stSubject;
            objEmail.Body = sbHTML.ToString();

            if (CCEmailAddress != "")
            {
                if (CCEmailAddress.Contains("@") == true && CCEmailAddress.Contains(".") == true)
                {
                    objEmail.CC.Add(CCEmailAddress);
                }
            }

            objEmail.IsBodyHtml = true;
            objEmail.Priority = MailPriority.High;

            if (stAttachmentFileName != null)
            {
                //   objEmail.Attachments.Add(new Attachment(stAttachmentFileName, SessionFacade.UserSession.User_name + "_" + DateTime.Now.ToString("MMddyyyy") + "_" + DateTime.Now.ToString("hhmmss"), MediaTypeNames.Application.Pdf));
            }

            if (isBCCRequired == true && !string.IsNullOrEmpty(RequestHelpers.GetConfigValue("CCMailID")))
            {
                string[] staBCCMails = RequestHelpers.GetConfigValue("CCMailID").Split(';');
                foreach (string stBccID in staBCCMails)
                {
                    if (!string.IsNullOrEmpty(stBccID.Trim()))
                    {
                        objEmail.Bcc.Add(stBccID.Trim());
                    }
                }
            }

            SmtpClient client = new SmtpClient();
            System.Net.NetworkCredential auth = new System.Net.NetworkCredential(stFromUserName, stFromPassword);
            client.Host = stHost;
            client.Port = inPort;
            client.EnableSsl = btIsSSL;
            client.DeliveryMethod = SmtpDeliveryMethod.Network;
            client.UseDefaultCredentials = false;
            client.Credentials = auth;

            client.Send(objEmail);

            return stReturnText;
        }

        public static string SendingMailWithAttachment(string stHtmlBody, string stSubject, string stAttachmentFileName,
                                    string stAttachmentFileNamePdf, string stToEmailAddresses, string CCEmailAddress, bool isBCCRequired)
        {
            string stReturnText = string.Empty;

            StringBuilder sbHTML = new StringBuilder();
            //sbHTML.Append(SetClasses());
            //  sbHTML.Append(SetHeader());
            sbHTML.Append(stHtmlBody);
            // sbHTML.Append(SetFooter());

            //Set SmtpClient to send Email
            string stFromUserName = RequestHelpers.GetConfigValue("FromUserName");
            string stFromPassword = RequestHelpers.GetConfigValue("FromPassword");
            int inPort = Convert.ToInt32(RequestHelpers.GetConfigValue("Port"));
            string stHost = RequestHelpers.GetConfigValue("Host");
            bool btIsSSL = Convert.ToBoolean(RequestHelpers.GetConfigValue("EnableSSL"));

            MailMessage objEmail = new MailMessage();
            MailAddress from = new MailAddress("\"" + RequestHelpers.GetConfigValue("Title") + "\" " + stFromUserName);
            objEmail.From = from;

            if (stToEmailAddresses.Contains(","))
            {
                string[] EmailIDs = stToEmailAddresses.Split(',');
                int i;
                for (i = 0; i < EmailIDs.Length; i++)
                {
                    if (EmailIDs[i].Contains("@") == true && EmailIDs[i].Contains(".") == true)
                    {
                        objEmail.To.Add(EmailIDs[i]);
                    }
                }
            }
            else
            {
                if (stToEmailAddresses.Contains("@") == true && stToEmailAddresses.Contains(".") == true)
                {
                    objEmail.To.Add(stToEmailAddresses);
                }
            }

            objEmail.Subject = stSubject;
            objEmail.Body = sbHTML.ToString();

            if (CCEmailAddress != "")
            {
                if (CCEmailAddress.Contains("@") == true && CCEmailAddress.Contains(".") == true)
                {
                    objEmail.CC.Add(CCEmailAddress);
                }
            }

            objEmail.IsBodyHtml = true;
            objEmail.Priority = MailPriority.High;

            if (stAttachmentFileName != "")
            {
                if (File.Exists(stAttachmentFileName))
                {
                    Attachment oAttachment = new Attachment(stAttachmentFileName);
                    objEmail.Attachments.Add(oAttachment);
                }

            }
            if (stAttachmentFileNamePdf != "")
            {
                if (File.Exists(stAttachmentFileNamePdf))
                {
                    Attachment oAttachmentPdf = new Attachment(stAttachmentFileNamePdf);
                    objEmail.Attachments.Add(oAttachmentPdf);
                }
            }

            if (isBCCRequired == true && !string.IsNullOrEmpty(RequestHelpers.GetConfigValue("CCMailID")))
            {
                string[] staBCCMails = RequestHelpers.GetConfigValue("CCMailID").Split(';');
                foreach (string stBccID in staBCCMails)
                {
                    if (!string.IsNullOrEmpty(stBccID.Trim()))
                    {
                        objEmail.Bcc.Add(stBccID.Trim());
                    }
                }
            }

            SmtpClient client = new SmtpClient();
            System.Net.NetworkCredential auth = new System.Net.NetworkCredential(stFromUserName, stFromPassword);
            client.Host = stHost;
            client.Port = inPort;
            client.EnableSsl = btIsSSL;
            client.DeliveryMethod = SmtpDeliveryMethod.Network;
            client.UseDefaultCredentials = false;
            client.Credentials = auth;

            client.Send(objEmail);

            return stReturnText;
        }




    }
}