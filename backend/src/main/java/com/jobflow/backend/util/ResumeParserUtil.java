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
        String name = lines.length > 0
                ? lines[0].trim()
                : "Unknown";

        // DYNAMIC SKILLS EXTRACTION
        List<String> detectedSkills = new ArrayList<>();

        String skillsSection = extractSection(
                text,
                "TECHNICAL SKILLS",
                new String[] {
                        "WORK EXPERIENCE",
                        "PROJECTS",
                        "EDUCATION"
                });

        String cleanedSkills = skillsSection
                .replace("\n", ",")
                .replace("Languages:", "")
                .replace("Frameworks:", "")
                .replace("Databases:", "")
                .replace("Tools:", "")
                .replace("Other:", "");

        String[] splitSkills = cleanedSkills.split(",");

        for (String skill : splitSkills) {

            String trimmedSkill = skill.trim();

            if (!trimmedSkill.isEmpty()
                    && trimmedSkill.length() > 1) {

                detectedSkills.add(trimmedSkill);
            }
        }

        // EMAIL
        String email = "Not Found";

        Pattern emailPattern = Pattern.compile(
                "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+");

        Matcher emailMatcher = emailPattern.matcher(text);

        if (emailMatcher.find()) {
            email = emailMatcher.group();
        }

        // PROFESSIONAL SUMMARY
        String professionalSummary = extractSection(
                text,
                "PROFESSIONAL SUMMARY",
                new String[] {
                        "TECHNICAL SKILLS",
                        "WORK EXPERIENCE",
                        "PROJECTS",
                        "EDUCATION"
                });

        // EXPERIENCE
        String experience = extractSection(
                text,
                "WORK EXPERIENCE",
                new String[] {
                        "PROJECTS",
                        "EDUCATION",
                        "CERTIFICATIONS"
                });

        // PROJECTS
        String projects = extractSection(
                text,
                "PROJECTS",
                new String[] {
                        "EDUCATION",
                        "CERTIFICATIONS"
                });

        // EDUCATION
        String education = extractSection(
                text,
                "EDUCATION",
                new String[] {
                        "CERTIFICATIONS",
                        "PROJECTS"
                });

        // CERTIFICATIONS
        String certifications = extractSection(
                text,
                "CERTIFICATIONS",
                new String[] {
                        "EDUCATION",
                        "PROJECTS"
                });

        return new ResumeData(
                name,
                detectedSkills,
                education,
                email,
                experience,
                professionalSummary,
                certifications,
                projects);
    }

    private static String extractSection(
            String text,
            String startHeader,
            String[] endHeaders) {

        String upperText = text.toUpperCase();

        int start = upperText.indexOf(startHeader.toUpperCase());

        if (start == -1) {
            return "Not Found";
        }

        start += startHeader.length();

        int end = text.length();

        for (String endHeader : endHeaders) {

            int tempEnd = upperText.indexOf(
                    endHeader.toUpperCase(),
                    start);

            if (tempEnd != -1 && tempEnd < end) {
                end = tempEnd;
            }
        }

        return text.substring(start, end).trim();
    }
}