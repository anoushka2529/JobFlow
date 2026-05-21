import React, { useState } from "react";

const moduleTitles = [
  "ATS SCORE EXPLANATION",
  "RESUME STRENGTHS",
  "AREAS FOR IMPROVEMENT",
  "ATS OPTIMIZATION",
  "BETTER BULLET POINT SUGGESTIONS",
  "SKILLS GAP ANALYSIS"
];

const stopTitles = [...moduleTitles, "PERSONALIZED INTERVIEW QUESTIONS"];

const escapeRegex = (text) => {
  return text.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
};

const splitAIAnalysis = (text) => {
  if (!text) return [];

  const cleanedText = text
    .replace(/=+/g, "")
    .replace(/\*/g, "")
    .replace(/#/g, "")
    .replace(/\r/g, "")
    .trim();

  const sections = [];

  moduleTitles.forEach((title) => {
    const headingRegex = new RegExp(
      `^\\s*${escapeRegex(title)}\\s*:?\\s*$`,
      "im"
    );

    const currentMatch = cleanedText.match(headingRegex);

    if (!currentMatch) return;

    const start = currentMatch.index + currentMatch[0].length;
    let end = cleanedText.length;

    stopTitles.forEach((stopTitle) => {
      if (stopTitle === title) return;

      const stopRegex = new RegExp(
        `^\\s*${escapeRegex(stopTitle)}\\s*:?\\s*$`,
        "im"
      );

      const remainingText = cleanedText.substring(start);
      const stopMatch = remainingText.match(stopRegex);

      if (stopMatch) {
        const stopIndex = start + stopMatch.index;

        if (stopIndex > start && stopIndex < end) {
          end = stopIndex;
        }
      }
    });

    const content = cleanedText
      .substring(start, end)
      .replace(/^[:\-\s]+/, "")
      .replace(/^\d+\.\s*/gm, "")
      .trim();

    const points = content
      .split(/\n+/)
      .map((point) => point.trim())
      .filter((point) => point.length > 0);

    sections.push({
      title,
      points
    });
  });

  return sections;
};

function AnalyzeResume({ user, selectedResume }) {
  const [file, setFile] = useState(null);
  const [jobDescription, setJobDescription] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const displayData = selectedResume || result;

  const score =
    displayData?.totalScore ??
    displayData?.atsScore?.totalScore ??
    0;

  const aiAnalysis = displayData?.aiAnalysis || "";
  const modules = splitAIAnalysis(aiAnalysis);
  const hasModules = modules.length > 0;

  const getModulePoints = (title) => {
    const section = modules.find((module) => module.title === title);
    return section ? section.points : [];
  };

  const atsExplanation = getModulePoints("ATS SCORE EXPLANATION");
  const strengths = getModulePoints("RESUME STRENGTHS");
  const improvements = getModulePoints("AREAS FOR IMPROVEMENT");
  const atsOptimization = getModulePoints("ATS OPTIMIZATION");
  const bulletSuggestions = getModulePoints("BETTER BULLET POINT SUGGESTIONS");
  const skillsGap = getModulePoints("SKILLS GAP ANALYSIS");

  const handleAnalyze = async () => {
    if (!file) {
      alert("Please upload a resume PDF");
      return;
    }

    if (!jobDescription.trim()) {
      alert("Please enter a job description");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);
    formData.append("jobDescription", jobDescription);
    formData.append("userId", user.userId);

    try {
      setLoading(true);

      const response = await fetch("http://localhost:8080/resume/upload", {
        method: "POST",
        body: formData
      });

      if (!response.ok) {
        throw new Error("Resume analysis failed");
      }

      const data = await response.json();
      setResult(data);
    } catch (error) {
      console.error(error);
      alert("Something went wrong while analyzing the resume");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1>Analyze Resume</h1>

      <p className="page-subtitle">
        {selectedResume
          ? "Viewing saved resume analysis."
          : "Upload your resume and paste a job description to get ATS feedback."}
      </p>

      {!selectedResume && (
        <div className="input-card">
          <label>Upload Resume PDF</label>

          <div className="custom-file-upload">
            <label className="upload-btn">
              Choose File
              <input
                type="file"
                accept=".pdf"
                onChange={(event) => setFile(event.target.files[0])}
              />
            </label>

            <span className="file-name">
              {file ? file.name : "No file selected"}
            </span>
          </div>

          <label>Job Description</label>

          <textarea
            placeholder="Paste the job description here..."
            value={jobDescription}
            onChange={(event) => setJobDescription(event.target.value)}
          />

          <button onClick={handleAnalyze} disabled={loading}>
            {loading ? "Analyzing..." : "Analyze Resume"}
          </button>
        </div>
      )}

      {displayData && (
        <div className="report-container">
          <div className="report-header">
            <div>
              <h1>Analysis Results</h1>
              <p>
                {displayData.fileName || file?.name || "Uploaded Resume"} •
                Completed
              </p>
            </div>
          </div>

          {hasModules ? (
            <div className="report-layout">
              <div className="report-main">
                <div className="report-card ats-report-card">
                  <div className="score-card-inner">
                    <div>
                      <h2>ATS Score</h2>

                      <div
                        className="score-circle"
                        style={{
                          background: `conic-gradient(#2563eb ${
                            score * 3.6
                          }deg, #e2e8f0 0deg)`
                        }}
                      >
                        <span>{score}</span>
                      </div>
                    </div>

                    <div className="ats-summary">
                      <h3>
                        {score >= 75 ? "Great job!" : "Needs improvement"}
                      </h3>
                      <p>
                        {atsExplanation[0] ||
                          "Your resume has been analyzed against the job description."}
                      </p>
                    </div>
                  </div>
                </div>

                <div className="report-card">
                  <h2>Score Breakdown</h2>

                  <div className="score-breakdown">
                    <div>
                      <span>Keywords</span>
                      <div className="bar">
                        <div
                          style={{ width: `${Math.min(score + 5, 100)}%` }}
                        ></div>
                      </div>
                      <strong>{Math.min(score + 5, 100)}%</strong>
                    </div>

                    <div>
                      <span>Skills</span>
                      <div className="bar">
                        <div
                          style={{ width: `${Math.min(score + 3, 100)}%` }}
                        ></div>
                      </div>
                      <strong>{Math.min(score + 3, 100)}%</strong>
                    </div>

                    <div>
                      <span>Experience</span>
                      <div className="bar">
                        <div style={{ width: `${score}%` }}></div>
                      </div>
                      <strong>{score}%</strong>
                    </div>

                    <div>
                      <span>Formatting</span>
                      <div className="bar">
                        <div
                          style={{ width: `${Math.max(score - 10, 0)}%` }}
                        ></div>
                      </div>
                      <strong>{Math.max(score - 10, 0)}%</strong>
                    </div>
                  </div>
                </div>

                <div className="report-two-column">
                  <div className="report-card">
                    <h2>Strengths</h2>
                    <ul className="clean-list success-list">
                      {strengths.slice(0, 5).map((point, index) => (
                        <li key={index}>{point}</li>
                      ))}
                    </ul>
                  </div>

                  <div className="report-card">
                    <h2>Areas for Improvement</h2>
                    <ul className="clean-list warning-list">
                      {improvements.slice(0, 5).map((point, index) => (
                        <li key={index}>{point}</li>
                      ))}
                    </ul>
                  </div>
                </div>

                <div className="report-card">
                  <h2>ATS Optimization</h2>
                  <ul className="clean-list">
                    {atsOptimization.map((point, index) => (
                      <li key={index}>{point}</li>
                    ))}
                  </ul>
                </div>

                <div className="report-card">
                  <h2>Better Bullet Point Suggestions</h2>
                  <ul className="clean-list">
                    {bulletSuggestions.map((point, index) => (
                      <li key={index}>{point}</li>
                    ))}
                  </ul>
                </div>
              </div>

              <div className="report-side">
                <div className="report-card">
                  <h2>Top Missing Skills</h2>

                  <div className="skill-chip-box">
                    {skillsGap.slice(0, 6).map((skill, index) => (
                      <span key={index}>{skill}</span>
                    ))}
                  </div>
                </div>

                <div className="report-card">
                  <h2>Job Match</h2>
                  <div className="mini-score">{score}%</div>
                  <p>{score >= 75 ? "Good Match" : "Partial Match"}</p>
                </div>
              </div>
            </div>
          ) : (
            <div className="report-card">
              <h2>AI Analysis</h2>
              <p className="analysis-fallback">{aiAnalysis}</p>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default AnalyzeResume;