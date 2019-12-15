package com.sina.service;

import com.sina.domain.Course;
import com.sina.map.CourseDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
@Service
public class CourseService {
    public static final Log logger = LogFactory.getLog(CourseService.class);

    @Autowired
    private CourseDao courseDao;


    public int saveCourse(Course tSchema)  {
        int recordCount = 0;
        recordCount = this.courseDao.insert(tSchema) ;
        return recordCount;
    }


    public int insertCourseList(List<Course> list) {
        int result;
        result = this.courseDao.insertCourseList(list);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int saveCourseList(List<Course> list)  {
        if (list == null || list.size() == 0) {
            return 0;
        }
        Course tSchema = null;
        for (int i = 0; i < list.size(); i++) {
            tSchema = list.get(i);
            this.courseDao.insert(tSchema);
        }
        return list.size();

    }

    public CourseDao getCourseDao() {
        return courseDao;
    }

    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }


}

