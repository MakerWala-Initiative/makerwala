using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApp.Areas.Admin.Models
{
    public class SearchFilter
    {

        public int User_Id { get; set; }
        public string Search { get; set; }
        public int StartIndex
        {
            get
            {
                return SetStartRecords();
            }
        }
        public int pageIndex { get; set; }
        private int _PageSize = 10;
        public int PageSize
        {
            get
            {
                return _PageSize;
            }
            set
            {
                _PageSize = value;
            }
        }

        public int SetStartRecords()
        {
            if (pageIndex != 1)
            {
                return (PageSize * pageIndex - (_PageSize - 1)) > 0 ? (PageSize * pageIndex - (_PageSize - 1)) : 0;
            }
            else
            {
                return 0;
            }
        }
    }

}