package com.example.pdbsflhr22.studentsystem;

/**
 * Created by malik on 1/9/2017.
 */
public class Second_Term
{

   // private String Announcement;

    private String studentIdentity;
    private String tmarks;
    private String objectId;
    private String status;
    private String created_by;




    //getting and setting Announcement

    public String getStudentIdentity() {
     return studentIdentity;
    }

    public void setAnnouncement( String studentIdentity ) {
     this.studentIdentity = studentIdentity;
    }


    //getting and setting created_by

    public String getCreated_by() {
     return created_by;
    }

    public void setAnnouncementBY( String created_by ) {
     this.created_by = created_by;
    }



    //getting and setting objectId

    public String getObjectId() {
    return objectId;
    }

    public void setObjectId( String objectId ) {
    this.objectId = objectId;
    }


    //getting and setting CreatedAt

    public String getStatus() {
    return status;
    }

    public void setCreatedAt( String status ) {
    this.status = status;
    }


    //getting and setting Title

    public String getTmarks() {
    return tmarks;
    }

    public void setTitle( String tmarks ) {
    this.tmarks = tmarks;
    }


}
