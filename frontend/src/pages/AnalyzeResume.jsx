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

const splitAIAnalysis = (text) => {
  if (!text) return [];

  return moduleTitles
    .map((title) => {
      const start = text.indexOf(title);
      if (start === -1) return null;

      const possibleEnds = stopTitles
        .filter((t) => t !== title)
        .map((t) => text.indexOf(t, start + title.length))
        .filter((i) => i !== -1);

      const end = possibleEnds.length > 0 ? Math.min(...possibleEnds) : text.length;

      let content = text.substring(start + title.length, end);

      content = content
        .replace(/=+/g, "")
        .replace(/\*/g, "")
        .replace(/^[:\-\s]+/, "")
        .replace(/\n\s*\n/g, "\n")
        .trim();

      const points = content
        .split("\n")
        .map((p) => p.trim())
        .filter((p) => p !== "");

      return {
        title,
        points
      };
    })
    .filter(Boolean);
};

function AnalyzeResume({ user }) {
  const [file, setFile] = useState(null);
  const [jobDescription, setJobDescription] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

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

  const modules = splitAIAnalysis(result?.aiAnalysis);

  return (
    <div>
      <h1>Analyze Resume</h1>
      <p className="page-subtitle">
        Upload your resume and paste a job description to get ATS feedback.
      </p>

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

      {result && (
        <div className="result-container">
          <div className="score-card">
            <h2>ATS Score</h2>

            <div
              className="score-circle"
              style={{
                background: `conic-gradient(#2563eb ${
                  result.atsScore.totalScore * 3.6
                }deg, #e2e8f0 0deg)`
              }}
            >
              <span>{result.atsScore.totalScore}</span>
            </div>
          </div>

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
        </div>
      )}
    </div>
  );
}

export default AnalyzeResume;