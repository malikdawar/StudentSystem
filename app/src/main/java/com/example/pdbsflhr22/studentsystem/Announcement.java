package com.example.pdbsflhr22.studentsystem;

/**
 * Created by malik on 1/9/2017.
 */
public class Announcement
{

   // private String Announcement;

    private String Announcement;
    private String Title;
    private String objectId;
    private String created;
    private String created_by;




    //getting and setting Announcement

    public String getAnnouncement() {
     return Announcement;
    }

    public void setAnnouncement( String Announcement ) {
     this.Announcement = Announcement;
    }


    //getting and setting created_by

    public String getAnnouncementBY() {
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

    public String getCreatedAt() {
    return created;
    }

    public void setCreatedAt( String created ) {
    this.created = created;
    }


    //getting and setting Title

    public String getTitle() {
    return Title;
    }

    public void setTitle( String Title ) {
    this.Title = Title;
    }


}
