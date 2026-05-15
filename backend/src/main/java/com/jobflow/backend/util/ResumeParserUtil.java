package com.jobflow.backend.util;

import com.jobflow.backend.dto.ResumeData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeParserUtil {

    public static ResumeData parseResume(String text) {

        String[] lines = text.split("\\r?\\n");

        // NAME
        String name = lines.length > 0 ? lines[0].trim() : "Unknown";

        // SKILLS
        List<String> detectedSkills = new ArrayList<>();

        String[] knownSkills = {
                "Java",
                "Spring Boot",
                "Python",
                "SQL",
                "Docker",
                "React",
                "JavaScript",
                "HTML",
                "CSS",
                "AWS",
                "Firebase",
                "Machine Learning",
                "Power BI",
                "Git",
                "PostgreSQL"
        };

        for (String skill : knownSkills) {

            if (text.toLowerCase().contains(skill.toLowerCase())) {
                detectedSkills.add(skill);
            }
        }

        // EDUCATION
        String education = extractSection(
                text,
                "education",
                new String[] {
                        "experience",
                        "skills",
                        "projects",
                        "certifications"
                });

        // EMAIL
        String email = "Not Found";

        Pattern emailPattern = Pattern.compile(
                "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+");

        Matcher emailMatcher = emailPattern.matcher(text);

        if (emailMatcher.find()) {
            email = emailMatcher.group();
        }

        // EXPERIENCE
        String experience = extractSection(
                text,
                "experience",
                new String[] {
                        "education",
                        "skills",
                        "projects",
                        "certifications"
                });

        // PROFESSIONAL SUMMARY
        String professionalSummary = extractSection(
                text,
                "summary",
                new String[] {
                        "experience",
                        "skills",
                        "education",
                        "projects",
                        "certifications"
                });

        // CERTIFICATIONS
        String certifications = extractSection(
                text,
                "certifications",
                new String[] {
                        "projects",
                        "skills",
                        "education",
                        "experience"
                });

        return new ResumeData(
                name,
                detectedSkills,
                education,
                email,
                experience,
                professionalSummary,
                certifications);
    }

    private static String extractSection(
            String text,
            String startKeyword,
            String[] endKeywords) {

        String lowerText = text.toLowerCase();

        int start = lowerText.indexOf(startKeyword.toLowerCase());

        if (start == -1) {
            return "Not Found";
        }

        int end = text.length();

        for (String endKeyword : endKeywords) {

            int tempEnd = lowerText.indexOf(
                    endKeyword.toLowerCase(),
                    start + startKeyword.length());

            if (tempEnd != -1 && tempEnd < end) {
                end = tempEnd;
            }
        }

        return text.substring(start, end).trim();
    }
}