package com.example.pdbsflhr22.studentsystem;

/**
 * Created by malik on 1/9/2017.
 */
public class Contact
{

    private String username;

    private String studentname;
    private String email;
    private String numb;
    private String rollnum;
    private String section;
    private String parentemail;
    private String parentnumb;
    private String gender;
    private String objectId;




    public String getObjectId() {
        return objectId;
    }

    public void setObjectId( String objectId ) {
        this.objectId = objectId;
    }



    //getting and setting UserName

    public String getName() {
     return studentname;
    }

    public void setName( String studentname ) {
     this.studentname = studentname;
    }



    //getting and setting PhoneNum

    public String getPhone() {
    return numb;
    }

    public void setPhone( String numb ) {
    this.numb = numb;
    }


    //getting and setting Bloodgroup

    public String getParentNum() {
    return parentnumb;
    }

    public void setParentNum( String parentnumb ) {
    this.parentnumb = parentnumb;
    }


    //getting and setting City

    public String getParentEmail() {
    return parentemail;
    }

    public void setParentEmail( String parentemail ) {
    this.parentemail = parentemail;
    }


    //getting and setting Gender

    public String getGender() {
    return gender;
    }

    public void setGender( String gender ) {
    this.gender = gender;
    }


    //getting and setting Email

        public String getEmail() {
    return email;
    }

    public void setEmail( String email ) {
    this.email = email;
    }



    //getting and setting Section

    public String getSection() {
    return section;
    }

    public void setSection( String section ) {
    this.section = section;
    }


    //getting and setting RollNum

    public String getRollNum() {
    return rollnum;
    }

    public void setRollNum( String rollnum ) {
    this.rollnum = rollnum;
    }
}
