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
    
    public partial class subject
    {
        public subject()
        {
            this.subjectclassmappings = new HashSet<subjectclassmapping>();
            this.teachersubjectmappings = new HashSet<teachersubjectmapping>();
            this.textbooks = new HashSet<textbook>();
            this.videodatas = new HashSet<videodata>();
        }
    
        public int subjectid { get; set; }
        public string subjectname { get; set; }
        public int createdby { get; set; }
        public System.DateTime createddate { get; set; }
        public Nullable<int> updatedby { get; set; }
        public Nullable<System.DateTime> updateddate { get; set; }
        public string remarks { get; set; }
        public bool isactive { get; set; }
    
        public virtual ICollection<subjectclassmapping> subjectclassmappings { get; set; }
        public virtual ICollection<teachersubjectmapping> teachersubjectmappings { get; set; }
        public virtual ICollection<textbook> textbooks { get; set; }
        public virtual ICollection<videodata> videodatas { get; set; }
    }
}
