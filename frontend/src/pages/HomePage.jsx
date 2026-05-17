import React, { useEffect, useState } from "react";
import "../App.css";

function HomePage({ user, onLogout }) {
  const [resumeFile, setResumeFile] = useState(null);
  const [jobDescription, setJobDescription] = useState("");
  const [analysis, setAnalysis] = useState(null);
  const [loading, setLoading] = useState(false);
  const [dashboard, setDashboard] = useState(null);

  const fetchDashboard = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/dashboard/${user.userId}`
      );

      const data = await response.json();
      setDashboard(data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchDashboard();
  }, []);

  const handleAnalyze = async () => {
    if (!resumeFile || !jobDescription.trim()) {
      alert("Please upload a resume and enter a job description.");
      return;
    }

    const formData = new FormData();
    formData.append("file", resumeFile);
    formData.append("jobDescription", jobDescription);
    formData.append("userId", user.userId);

    try {
      setLoading(true);

      const response = await fetch("http://localhost:8080/resume/upload", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        throw new Error("Failed to analyze resume");
      }

      const data = await response.json();
      setAnalysis(data);
      fetchDashboard();
    } catch (error) {
      console.error(error);
      alert("Something went wrong while analyzing the resume.");
    } finally {
      setLoading(false);
    }
  };

  const cleanText = (text) => {
    return text
      .replace(/^[-•*]\s*/, "")
      .replace(/^\d+\.\s*/, "")
      .replace(/\*\*/g, "")
      .replace(/=+/g, "")
      .trim();
  };

  const normalizeTitle = (title) => {
    return title.toLowerCase().replace(/[^a-z0-9]/g, "");
  };

  const parseAISections = (text) => {
    if (!text) return [];

    const moduleTitles = [
      "ATS SCORE EXPLANATION",
      "RESUME STRENGTHS",
      "AREAS FOR IMPROVEMENT",
      "ATS OPTIMIZATION",
      "BETTER BULLET POINT SUGGESTIONS",
      "SKILLS GAP ANALYSIS",
      "PERSONALIZED INTERVIEW QUESTIONS",
    ];

    const lines = text
      .split("\n")
      .map((line) => cleanText(line))
      .filter(Boolean);

    const sections = [];
    let currentSection = null;

    lines.forEach((line) => {
      const normalizedLine = normalizeTitle(line);

      const matchedTitle = moduleTitles.find(
        (title) => normalizeTitle(title) === normalizedLine
      );

      if (matchedTitle) {
        if (currentSection) {
          sections.push(currentSection);
        }

        currentSection = {
          title: matchedTitle,
          content: [],
        };
      } else if (currentSection) {
        currentSection.content.push(line);
      }
    });

    if (currentSection) {
      sections.push(currentSection);
    }

    return sections;
  };

  const getSectionByTitle = (sections, title) => {
    const requiredTitle = normalizeTitle(title);

    return (
      sections.find(
        (section) => normalizeTitle(section.title) === requiredTitle
      ) || {
        title,
        content: ["No content generated for this section."],
      }
    );
  };

  const isInterviewSubheading = (line) => {
    const lower = line.toLowerCase();

    return (
      lower.includes("technical questions") ||
      lower.includes("project-based questions") ||
      lower.includes("project based questions") ||
      lower.includes("behavioral questions") ||
      lower.includes("backend/system design questions") ||
      lower.includes("backend questions") ||
      lower.includes("system design questions")
    );
  };

  const renderNormalModule = (section) => {
    return (
      <ul className="point-list">
        {section.content
          .filter((point) => point.trim() !== "")
          .map((point, index) => (
            <li key={index}>{point}</li>
          ))}
      </ul>
    );
  };

  const renderInterviewModule = (section) => {
    const groups = [];
    let currentGroup = {
      heading: "",
      questions: [],
    };

    section.content.forEach((line) => {
      if (isInterviewSubheading(line)) {
        if (currentGroup.heading || currentGroup.questions.length > 0) {
          groups.push(currentGroup);
        }

        currentGroup = {
          heading: line,
          questions: [],
        };
      } else {
        currentGroup.questions.push(line);
      }
    });

    if (currentGroup.heading || currentGroup.questions.length > 0) {
      groups.push(currentGroup);
    }

    return (
      <div className="interview-section">
        {groups.map((group, index) => (
          <div className="question-group" key={index}>
            {group.heading && (
              <div className="question-heading">{group.heading}</div>
            )}

            <ul className="interview-points">
              {group.questions.map((question, questionIndex) => (
                <li key={questionIndex}>{question}</li>
              ))}
            </ul>
          </div>
        ))}
      </div>
    );
  };

  const getScoreColor = (score) => {
    if (score >= 80) return "#16a34a";
    if (score >= 60) return "#f59e0b";
    return "#dc2626";
  };

  const score = analysis?.atsScore?.totalScore || 0;
  const scoreColor = getScoreColor(score);

  const aiSections = parseAISections(analysis?.aiAnalysis);

  const fixedModules = [
    "ATS SCORE EXPLANATION",
    "RESUME STRENGTHS",
    "AREAS FOR IMPROVEMENT",
    "ATS OPTIMIZATION",
    "BETTER BULLET POINT SUGGESTIONS",
    "SKILLS GAP ANALYSIS",
    "PERSONALIZED INTERVIEW QUESTIONS",
  ];

  return (
    <div className="app">
      <div className="main-container">
        <h1>JobFlow AI</h1>

        <div className="user-header">
          <p>Welcome, {user.name}</p>

          <button className="logout-btn" onClick={onLogout}>
            Logout
          </button>
        </div>

        <p className="subtitle">
          AI-powered resume analysis, ATS scoring, and interview preparation.
        </p>

        {dashboard && (
          <>
            <div className="dashboard-grid">
              <div className="dashboard-card">
                <h3>Total Resumes</h3>
                <p>{dashboard.totalResumes}</p>
              </div>

              <div className="dashboard-card">
                <h3>Average Score</h3>
                <p>{dashboard.averageScore}%</p>
              </div>

              <div className="dashboard-card">
                <h3>Highest Score</h3>
                <p>{dashboard.highestScore}%</p>
              </div>

              <div className="dashboard-card">
                <h3>Trend</h3>
                <p>{dashboard.scoreTrend}</p>
              </div>
            </div>

            <div className="module-card">
              <h2>Recent Resume History</h2>

              <div className="history-list">
                {dashboard.analyses?.map((item, index) => (
                  <div className="history-item" key={index}>
                    <div>
                      <strong>{item.fileName}</strong>
                    </div>

                    <div>{item.totalScore}%</div>

                    <div>{new Date(item.createdAt).toLocaleDateString()}</div>
                  </div>
                ))}
              </div>
            </div>
          </>
        )}

        <div className="input-card">
          <label>Upload Resume</label>

          <div className="custom-file-upload">
            <label htmlFor="resumeUpload" className="upload-btn">
              📄 Choose Resume
            </label>

            <input
              id="resumeUpload"
              type="file"
              accept=".pdf"
              onChange={(e) => setResumeFile(e.target.files[0])}
            />

            <span className="file-name">
              {resumeFile ? resumeFile.name : "No file selected"}
            </span>
          </div>

          <label>Job Description</label>

          <textarea
            placeholder="Paste the job description here..."
            value={jobDescription}
            onChange={(e) => setJobDescription(e.target.value)}
          />

          <button onClick={handleAnalyze} disabled={loading}>
            {loading ? "Analyzing..." : "Analyze Resume"}
          </button>
        </div>

        {analysis && (
          <div className="result-container">
            <div className="score-card">
              <h2>ATS Score</h2>

              <div
                className="score-circle"
                style={{
                  background: `conic-gradient(${scoreColor} ${
                    score * 3.6
                  }deg, #dbeafe 0deg)`,
                }}
              >
                <span>{score}%</span>
              </div>

              <div className="score-grid">
                <div className="score-box">
                  <h4>Skills Match</h4>
                  <p>{analysis.atsScore?.skillsScore}/20</p>
                </div>

                <div className="score-box">
                  <h4>Resume Structure</h4>
                  <p>{analysis.atsScore?.structureScore}/20</p>
                </div>

                <div className="score-box">
                  <h4>Experience Quality</h4>
                  <p>{analysis.atsScore?.experienceScore}/20</p>
                </div>

                <div className="score-box">
                  <h4>Projects Quality</h4>
                  <p>{analysis.atsScore?.projectsScore}/20</p>
                </div>

                <div className="score-box">
                  <h4>Keyword Optimization</h4>
                  <p>{analysis.atsScore?.keywordScore}/20</p>
                </div>
              </div>
            </div>

            <div className="module-card">
              <h2>Parsed Resume Data</h2>

              <p>
                <strong>Name:</strong> {analysis.resumeData?.name}
              </p>

              <p>
                <strong>Email:</strong> {analysis.resumeData?.email}
              </p>

              <h3 className="skills-title">Skills</h3>

              <div className="skills">
                {analysis.resumeData?.skills?.map((skill, index) => (
                  <span className="skill-tag" key={index}>
                    {skill}
                  </span>
                ))}
              </div>
            </div>

            {fixedModules.map((moduleTitle) => {
              const section = getSectionByTitle(aiSections, moduleTitle);

              return (
                <div className="module-card" key={moduleTitle}>
                  <h2>{moduleTitle}</h2>

                  {moduleTitle === "PERSONALIZED INTERVIEW QUESTIONS"
                    ? renderInterviewModule(section)
                    : renderNormalModule(section)}
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}

export default HomePage;