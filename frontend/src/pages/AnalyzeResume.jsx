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
                onChange={(e) => setFile(e.target.files[0])}
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
            onChange={(e) => setJobDescription(e.target.value)}
          />

          <button onClick={handleAnalyze} disabled={loading}>
            {loading ? "Analyzing..." : "Analyze Resume"}
          </button>
        </div>
      )}

      {displayData && (
        <div className="result-container">
          <div className="score-card">
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

                  {hasModules ? (
            <div className="analysis-grid">
              {modules.map((module, index) => (
                <div className="analysis-module-card" key={index}>
                  <h2>{module.title}</h2>

                  <ul className="analysis-points">
                    {module.points.map((point, i) => (
                      <li key={i}>{point}</li>
                    ))}
                  </ul>
                </div>
              ))}
            </div>
          ) : (
            <div className="analysis-module-card">
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