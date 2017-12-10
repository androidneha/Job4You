package in.co.job4you;

public class JobGetterSetter {
    private String company_name;
    private String job_title;
    private String website_url,id;
    private  String job_for_experience,location,required_qualification,sort_description;

    public String getJob_title() {
        return job_title;
    }
    public String getCompanyName()
    {
        return company_name;
    }

    public String getUrl() {
        return website_url;
    }
    public String getExp() {
        return job_for_experience;
    }
    public String getLocation() {
        return location;
    }
    public String getRequired_qualification() {
        return required_qualification;
    }
    public String getSort_description() {
        return sort_description;
    }
    public String getId() {
        return id;
    }
}
