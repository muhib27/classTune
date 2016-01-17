package com.classtune.app.schoolapp.model;

import com.classtune.app.schoolapp.utils.AppUtility;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FreeVersionPost {

	@SerializedName("title")
	private String title;

	@SerializedName("title_color")
	private String titleColor;

    public String getIsSpellingbee() {
        return isSpellingbee;
    }

    public void setIsSpellingbee(String isSpellingbee) {
        this.isSpellingbee = isSpellingbee;
    }

    @SerializedName("is_spelling_bee")
	private String isSpellingbee;

    public List<FreeVersionPost> getSubpost() {
        return subpost;
    }

    public void setSubpost(List<FreeVersionPost> subpost) {
        this.subpost = subpost;
    }

    @SerializedName("related_news_spelling_bee")
    private List<FreeVersionPost> subpost;

	@SerializedName("author")
	private String author;

	@SerializedName("id")
	private String id;

	@SerializedName("summary")
	private String summary;

	@SerializedName("content")
	private String content;

	@SerializedName("solution")
	private String solution;

	@SerializedName("mobile_image")
	private String mobileImageUrl;

	@SerializedName("published_date")
	private String publishedDate;

	@SerializedName("current_date")
	private String currentDate;

	@SerializedName("published_date_string")
	private String publishedDateString;

	@SerializedName("category_menu_icon")
	private String categoryMenuIconUrl;

	@SerializedName("category_icon")
	private String categoryIconUrl;

	@SerializedName("category_name")
	private String categoryName;

	@SerializedName("category_id")
	private String categoryId;

	@SerializedName("seen")
	private String seenCount;

	@SerializedName("post_type")
	private String postType;

	@SerializedName("second_category_name")
	private String secondCategoryName;

	@SerializedName("images")
	private ArrayList<String> images;

	@SerializedName("add_images")
	private ArrayList<AddData> add_images;

	@SerializedName("share_link")
	private ShareLink shareLink;

	@SerializedName("attach_content")
	private String attachContent;

	@SerializedName("attach_download_link")
	private String attachDownloadLink;

	@SerializedName("embedded_url")
	private String embeddedUrl;

	@SerializedName("view_count")
	private String viewCount;

	@SerializedName("video_file")
	private String videoUrl;

	@SerializedName("comments_total")
	private String commentCount;

	@SerializedName("full_content")
	private String fullContent;

	@SerializedName("wow_count")
	private String wow_count;

	@SerializedName("can_wow")
	private int can_wow;

	@SerializedName("can_comment")
	private String can_comment;

	@SerializedName("force_assessment")
	private String forceAssessment;

	@SerializedName("assessment_id")
	private String assessment_id;

	@SerializedName("can_share")
	private int can_share;
	
	@SerializedName("category_id_to_use")
	private String category_id_to_use;
	
	@SerializedName("subcategory_id_to_use")
	private String subcategory_id_to_use;
	
	public String getCategory_id_to_use() {
		return category_id_to_use;
	}

	public void setCategory_id_to_use(String category_id_to_use) {
		this.category_id_to_use = category_id_to_use;
	}

	public String getSubcategory_id_to_use() {
		return subcategory_id_to_use;
	}

	public void setSubcategory_id_to_use(String subcategory_id_to_use) {
		this.subcategory_id_to_use = subcategory_id_to_use;
	}

	private String coolFormatSeenCount;

	public String getCoolFormatSeenCount() {
		return coolFormatSeenCount;
	}

	public void setCoolFormatSeenCount(String coolFormatSeenCount) {
		this.coolFormatSeenCount = coolFormatSeenCount;
	}

	public int getCan_share() {
		return can_share;
	}

	public void setCan_share(int can_share) {
		this.can_share = can_share;
	}

	public String getAssessment_id() {
		return assessment_id;
	}

	public void setAssessment_id(String assessment_id) {
		this.assessment_id = assessment_id;
	}

	public String getForceAssessment() {
		return forceAssessment;
	}

	public void setForceAssessment(String forceAssessment) {
		this.forceAssessment = forceAssessment;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getCan_comment() {
		return can_comment;
	}

	public void setCan_comment(String can_comment) {
		this.can_comment = can_comment;
	}

	public String getWow_count() {
		return wow_count;
	}

	public void setWow_count(String wow_count) {
		this.wow_count = wow_count;
	}

	public int getCan_wow() {
		return can_wow;
	}

	public void setCan_wow(int can_wow) {
		this.can_wow = can_wow;
	}

	public String getFullContent() {
		return fullContent;
	}

	public void setFullContent(String fullContent) {
		this.fullContent = fullContent;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getViewCount() {
		return viewCount;
	}

	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}

	public String getEmbeddedUrl() {
		return embeddedUrl;
	}

	public void setEmbeddedUrl(String embeddedUrl) {
		this.embeddedUrl = embeddedUrl;
	}

	@SerializedName("image")
	private String image;

	public String getImage() {
		return image;
	}

	@SerializedName("normal_post_type")
	private int normal_post_type;

	@SerializedName("inside_image")
	private String inside_image;

	@SerializedName("short_title")
	private String short_title;

	@SerializedName("author_image")
	private String author_image;

	public String getAuthor_image() {
		return author_image;
	}

	public void setAuthor_image(String author_image) {
		this.author_image = author_image;
	}

	public int getNormal_post_type() {
		return normal_post_type;
	}

	public void setNormal_post_type(int normal_post_type) {
		this.normal_post_type = normal_post_type;
	}

	public String getInside_image() {
		return inside_image;
	}

	public void setInside_image(String inside_image) {
		this.inside_image = inside_image;
	}

	public String getShort_title() {
		return short_title;
	}

	public void setShort_title(String short_title) {
		this.short_title = short_title;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public ShareLink getShareLink() {
		return shareLink;
	}

	public void setShareLink(ShareLink shareLink) {
		this.shareLink = shareLink;
	}

	public ArrayList<AddData> getAdd_images() {
		return add_images;
	}

	public void setAdd_images(ArrayList<AddData> add_images) {
		this.add_images = add_images;
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	private List<Tags> tags = new ArrayList<Tags>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getMobileImageUrl() {
		return mobileImageUrl;
	}

	public void setMobileImageUrl(String mobileImageUrl) {
		this.mobileImageUrl = mobileImageUrl;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getPublishedDateString() {
		return publishedDateString;
	}

	public void setPublishedDateString(String publishedDateString) {
		this.publishedDateString = publishedDateString;
	}

	public String getCategoryMenuIconUrl() {
		return categoryMenuIconUrl;
	}

	public void setCategoryMenuIconUrl(String categoryMenuIconUrl) {
		this.categoryMenuIconUrl = categoryMenuIconUrl;
	}

	public String getCategoryIconUrl() {
		return categoryIconUrl;
	}

	public void setCategoryIconUrl(String categoryIconUrl) {
		this.categoryIconUrl = categoryIconUrl;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryId() {
		if (categoryId.equals(""))
			return "-1";
		else
			return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getSeenCount() {
		return seenCount;
	}

	public void setSeenCount(String seenCount) {
		this.seenCount = seenCount;
		setCoolFormatSeenCount(AppUtility.coolFormat(Double.parseDouble(seenCount), 0));
	}

	public List<Tags> getTags() {
		return tags;
	}

	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	public String getSecondCategoryName() {
		return secondCategoryName;
	}

	public void setSecondCategoryName(String secondCategoryName) {
		this.secondCategoryName = secondCategoryName;
	}

	public String getAttachContent() {
		return attachContent;
	}

	public void setAttachContent(String attachContent) {
		this.attachContent = attachContent;
	}

	public String getAttachDownloadLink() {
		return attachDownloadLink;
	}

	public void setAttachDownloadLink(String attachDownloadLink) {
		this.attachDownloadLink = attachDownloadLink;
	}

	public class Tags {

		@SerializedName("name")
		private String name;

		@SerializedName("id")
		private String id;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

	public class Categories {
		@SerializedName("title")
		private String title;

		@SerializedName("id")
		private String id;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

	public class Keywords {
		@SerializedName("title")
		private String title;

		@SerializedName("id")
		private String id;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

	public class Authors {
		@SerializedName("title")
		private String title;

		@SerializedName("id")
		private String id;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

	public class Attachment {

		@SerializedName("attach")
		public String attach;

		@SerializedName("content")
		public String content;

		@SerializedName("download_link")
		public String downloadLink;

		@SerializedName("caption")
		public String caption;

		@SerializedName("show")
		public String show;

		public String getAttach() {
			return attach;
		}

		public void setAttach(String attach) {
			this.attach = attach;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getDownloadLink() {
			return downloadLink;
		}

		public void setDownloadLink(String downloadLink) {
			this.downloadLink = downloadLink;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public String getShow() {
			return show;
		}

		public void setShow(String show) {
			this.show = show;
		}

	}

	public class Language {

		@SerializedName("language")
		public String language;

		@SerializedName("id")
		public String id;

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}
	
	public class LastVisited{
		@SerializedName("first")
		private String first;
		@SerializedName("number")
		private String number;
		@SerializedName("type")
		private String type;
		public String getFirst() {
			return first;
		}
		public void setFirst(String first) {
			this.first = first;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
	public class SummeryDate{
		@SerializedName("index")
		private int index;
		@SerializedName("number")
		private String number;
		@SerializedName("name")
		private String name;
		@SerializedName("dateformat")
		private String dateformat;
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDateformat() {
			return dateformat;
		}
		public void setDateformat(String dateformat) {
			this.dateformat = dateformat;
		}
		
	}
	public class HomeWorkSubject{
		@SerializedName("id")
		private String id;
		@SerializedName("name")
		private String name;
		@SerializedName("icon")
		private String icon;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
	}
	public class SummeryLeave{
		@SerializedName("id")
		private String id;
		@SerializedName("subject")
		private String subject;
		@SerializedName("is_read")
		private String is_read;
		@SerializedName("body")
		private String body;
		@SerializedName("rtype")
		private String rtype;
		@SerializedName("rid")
		private String rid;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public String getIs_read() {
			return is_read;
		}
		public void setIs_read(String is_read) {
			this.is_read = is_read;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public String getRtype() {
			return rtype;
		}
		public void setRtype(String rtype) {
			this.rtype = rtype;
		}
		public String getRid() {
			return rid;
		}
		public void setRid(String rid) {
			this.rid = rid;
		}
		
	}
    public class NextHomeWork{
        @SerializedName("subjects")
        private String subjects;
        @SerializedName("batch")
        private String batch;
        @SerializedName("course")
        private String course;
        @SerializedName("subjects_id")
        private String subjects_id;
        @SerializedName("duedate")
        private String duedate;
        @SerializedName("type")
        private String type;
        @SerializedName("id")
        private String id;

        public String getSubjects() {
            return subjects;
        }

        public void setSubjects(String subjects) {
            this.subjects = subjects;
        }

        public String getBatch() {
            return batch;
        }

        public void setBatch(String batch) {
            this.batch = batch;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getSubjects_id() {
            return subjects_id;
        }

        public void setSubjects_id(String subjects_id) {
            this.subjects_id = subjects_id;
        }

        public String getDuedate() {
            return duedate;
        }

        public void setDuedate(String duedate) {
            this.duedate = duedate;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDone() {
            return done;
        }

        public void setDone(String done) {
            this.done = done;
        }

        @SerializedName("done")
        private String done;

    }
    public class NextClass{
        @SerializedName("batch_name")
        private String batch_name;
        @SerializedName("course_name")
        private String course_name;
        @SerializedName("subject_code")
        private String subject_code;
        @SerializedName("subject_name")
        private String subject_name;
        @SerializedName("subject_icon_name")
        private String subject_icon_name;
        @SerializedName("subject_icon_path")
        private String subject_icon_path;
        @SerializedName("class_start_time")
        private String class_start_time;
        @SerializedName("class_end_time")
        private String class_end_time;

        public String getWeekday_text() {
            return weekday_text;
        }

        public void setWeekday_text(String weekday_text) {
            this.weekday_text = weekday_text;
        }

        public String getClass_end_time() {
            return class_end_time;
        }

        public void setClass_end_time(String class_end_time) {
            this.class_end_time = class_end_time;
        }

        public String getClass_start_time() {
            return class_start_time;
        }

        public void setClass_start_time(String class_start_time) {
            this.class_start_time = class_start_time;
        }

        public String getSubject_icon_path() {
            return subject_icon_path;
        }

        public void setSubject_icon_path(String subject_icon_path) {
            this.subject_icon_path = subject_icon_path;
        }

        public String getSubject_icon_name() {
            return subject_icon_name;
        }

        public void setSubject_icon_name(String subject_icon_name) {
            this.subject_icon_name = subject_icon_name;
        }

        public String getSubject_name() {
            return subject_name;
        }

        public void setSubject_name(String subject_name) {
            this.subject_name = subject_name;
        }

        public String getSubject_code() {
            return subject_code;
        }

        public void setSubject_code(String subject_code) {
            this.subject_code = subject_code;
        }

        public String getCourse_name() {
            return course_name;
        }

        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }

        public String getBatch_name() {
            return batch_name;
        }

        public void setBatch_name(String batch_name) {
            this.batch_name = batch_name;
        }

        @SerializedName("weekday_text")
        private String weekday_text;

    }
	public class DateFeed{
		@SerializedName("student_name")
		private String student_name;
		@SerializedName("attendence")
		private String attendence;
		@SerializedName("class_tommrow")
		private boolean hasClassTomorrow;
		@SerializedName("homework_subject")
		private ArrayList<HomeWorkSubject> homeWorkSubjects;
		@SerializedName("homework_total")
		private int homeworkTotal;
		@SerializedName("notice_total")
		private int noticeTotal;
		@SerializedName("result_publish")
		private String result_publish;
		@SerializedName("routine_publish")
		private String routine_publish;
		@SerializedName("event_tommorow")
		private boolean hasEventTomorrow;
		@SerializedName("event_id")
		private String event_id;
		@SerializedName("event_name")
		private String event_name;
		@SerializedName("exam_tommorow")
		private boolean hasExamTomorrow;
		@SerializedName("notice")
		private boolean hasNotice;
		@SerializedName("quiz")
		private ArrayList<SummeryQuiz> summeryQuizes;
		@SerializedName("leave")
		private ArrayList<SummeryLeave> summeryLeaves;
		@SerializedName("fees")
		private boolean fees;
		@SerializedName("meeting_request")
		private ArrayList<SummeryLeave> summeryMeetings;

        public boolean isMeetingTomorrow() {
            return meetingTomorrow;
        }

        public void setMeetingTomorrow(boolean meetingTomorrow) {
            this.meetingTomorrow = meetingTomorrow;
        }

        @SerializedName("meeting_tommorow")
        private boolean meetingTomorrow;
        public ArrayList<NextHomeWork> getNextHomeWorks() {
            return nextHomeWorks;
        }

        public void setNextHomeWorks(ArrayList<NextHomeWork> nextHomeWorks) {
            this.nextHomeWorks = nextHomeWorks;
        }
        @SerializedName("homework")
        private ArrayList<NextHomeWork> nextHomeWorks;

        public ArrayList<NextClass> getNextClasses() {
            return nextClasses;
        }

        public void setNextClasses(ArrayList<NextClass> nextClasses) {
            this.nextClasses = nextClasses;
        }

        @SerializedName("next_class")
        private ArrayList<NextClass> nextClasses;
		public ArrayList<SummeryLeave> getSummeryMeetings() {
			return summeryMeetings;
		}
		public void setSummeryMeetings(ArrayList<SummeryLeave> summeryMeetings) {
			this.summeryMeetings = summeryMeetings;
		}
		public boolean isFees() {
			return fees;
		}
		public void setFees(boolean fees) {
			this.fees = fees;
		}
		public ArrayList<SummeryLeave> getSummeryLeaves() {
			return summeryLeaves;
		}
		public void setSummeryLeaves(ArrayList<SummeryLeave> summeryLeaves) {
			this.summeryLeaves = summeryLeaves;
		}
		public ArrayList<SummeryQuiz> getSummeryQuizes() {
			return summeryQuizes;
		}
		public void setSummeryQuizes(ArrayList<SummeryQuiz> summeryQuizes) {
			this.summeryQuizes = summeryQuizes;
		}
		public String getStudent_name() {
			return student_name;
		}
		public void setStudent_name(String student_name) {
			this.student_name = student_name;
		}
		public String getAttendence() {
			return attendence;
		}
		public void setAttendence(String attendence) {
			this.attendence = attendence;
		}
		public boolean isHasClassTomorrow() {
			return hasClassTomorrow;
		}
		public void setHasClassTomorrow(boolean hasClassTomorrow) {
			this.hasClassTomorrow = hasClassTomorrow;
		}
		public ArrayList<HomeWorkSubject> getHomeWorkSubjects() {
			return homeWorkSubjects;
		}
		public void setHomeWorkSubjects(ArrayList<HomeWorkSubject> homeWorkSubjects) {
			this.homeWorkSubjects = homeWorkSubjects;
		}
		public String getResult_publish() {
			return result_publish;
		}
		public void setResult_publish(String result_publish) {
			this.result_publish = result_publish;
		}
		public String getRoutine_publish() {
			return routine_publish;
		}
		public void setRoutine_publish(String routine_publish) {
			this.routine_publish = routine_publish;
		}
		public boolean isHasEventTomorrow() {
			return hasEventTomorrow;
		}
		public void setHasEventTomorrow(boolean hasEventTomorrow) {
			this.hasEventTomorrow = hasEventTomorrow;
		}
		public String getEvent_id() {
			return event_id;
		}
		public void setEvent_id(String event_id) {
			this.event_id = event_id;
		}
		public String getEvent_name() {
			return event_name;
		}
		public void setEvent_name(String event_name) {
			this.event_name = event_name;
		}
		public boolean isHasExamTomorrow() {
			return hasExamTomorrow;
		}
		public void setHasExamTomorrow(boolean hasExamTomorrow) {
			this.hasExamTomorrow = hasExamTomorrow;
		}
		public boolean isHasNotice() {
			return hasNotice;
		}
		public void setHasNotice(boolean hasNotice) {
			this.hasNotice = hasNotice;
		}

		public int getHomeworkTotal() {
			return homeworkTotal;
		}

		public void setHomeworkTotal(int homeworkTotal) {
			this.homeworkTotal = homeworkTotal;
		}

		public int getNoticeTotal() {
			return noticeTotal;
		}

		public void setNoticeTotal(int noticeTotal) {
			this.noticeTotal = noticeTotal;
		}
	}
	/*********************************************
	 * SUMMERY FEED MODEL
	 *********************************************/
	@SerializedName("school_name")
	private String school_name;
	@SerializedName("school_picture")
	private String school_picture;
	@SerializedName("profile_picture")
	private String profile_picture;
	@SerializedName("last_visited")
	private LastVisited last_visited;
	@SerializedName("dates")
	private ArrayList<SummeryDate> summeryDates;
	@SerializedName("datefeed")
	private ArrayList<DateFeed> dateFeeds;

	private int currentSummeryPosition = 0;
	
	
	public int getCurrentSummeryPosition() {
		return currentSummeryPosition;
	}

	public void setCurrentSummeryPosition(int currentSummeryPosition) {
		this.currentSummeryPosition = currentSummeryPosition;
	}

	public ArrayList<DateFeed> getDateFeeds() {
		return dateFeeds;
	}

	public void setDateFeeds(ArrayList<DateFeed> dateFeeds) {
		this.dateFeeds = dateFeeds;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public String getSchool_picture() {
		return school_picture;
	}

	public void setSchool_picture(String school_picture) {
		this.school_picture = school_picture;
	}

	public String getProfile_picture() {
		return profile_picture;
	}

	public void setProfile_picture(String profile_picture) {
		this.profile_picture = profile_picture;
	}

	public LastVisited getLast_visited() {
		return last_visited;
	}

	public void setLast_visited(LastVisited last_visited) {
		this.last_visited = last_visited;
	}

	public ArrayList<SummeryDate> getSummeryDates() {
		return summeryDates;
	}

	public void setSummeryDates(ArrayList<SummeryDate> summeryDates) {
		this.summeryDates = summeryDates;
	}
	
}
