//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace Model
{
    using System;
    using System.Collections.Generic;
    
    public partial class transactionlog
    {
        public long transid { get; set; }
        public System.DateTime transdatetime { get; set; }
        public int transtypeid { get; set; }
        public Nullable<int> typeid { get; set; }
        public string commenttext { get; set; }
        public byte rating { get; set; }
        public long refid { get; set; }
        public int reftypeid { get; set; }
        public int createdby { get; set; }
        public System.DateTime createddate { get; set; }
        public bool isedited { get; set; }
        public int editedby { get; set; }
        public bool isdeleted { get; set; }
        public int deletedby { get; set; }
    }
}
