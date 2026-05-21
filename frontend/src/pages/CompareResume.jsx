import React, { useEffect, useState } from "react";
import {
  Radar,
  RadarChart,
  PolarGrid,
  PolarAngleAxis,
  PolarRadiusAxis,
  ResponsiveContainer,
  Legend
} from "recharts";

function CompareResume({ user }) {
  const [resumes, setResumes] = useState([]);
  const [firstId, setFirstId] = useState("");
  const [secondId, setSecondId] = useState("");

  useEffect(() => {
    fetch(`http://localhost:8080/resume/history/${user.userId}`)
      .then((res) => res.json())
       .then((data) => {
  console.log("Resume comparison data:", data);
  setResumes(Array.isArray(data) ? data : []);
})
      .catch((err) => {
        console.error("Compare error:", err);
        setResumes([]);
      });
  }, [user.userId]);

  const firstResume = resumes.find((r) => String(r.id) === firstId);
  const secondResume = resumes.find((r) => String(r.id) === secondId);

  const buildScoreBreakdown = (resume) => ({
  keywords: resume.keywordScore ?? 0,
  skills: resume.skillsScore ?? 0,
  experience: resume.experienceScore ?? 0,
  projects: resume.projectsScore ?? 0,
  structure: resume.structureScore ?? 0,
  overall: resume.totalScore ?? 0
});

  const firstScores = firstResume ? buildScoreBreakdown(firstResume) : null;
  const secondScores = secondResume ? buildScoreBreakdown(secondResume) : null;

  const chartData =
    firstScores && secondScores
      ? [
          {
            dimension: "Keywords",
            Resume1: firstScores.keywords * 5,
            Resume2: secondScores.keywords * 5
          },
          {
            dimension: "Skills",
            Resume1: firstScores.skills * 5,
            Resume2: secondScores.skills * 5
          },
          {
            dimension: "Experience",
            Resume1: firstScores.experience * 5,
            Resume2: secondScores.experience * 5
          },
          {
            dimension: "Projects",
            Resume1: firstScores.projects * 5,
            Resume2: secondScores.projects * 5
          },
          {
            dimension: "Structure",
            Resume1: firstScores.structure * 5,
            Resume2: secondScores.structure * 5
          }
        ]
      : [];

  const summaryRows =
    firstScores && secondScores
      ? [
          ["Keywords", firstScores.keywords, secondScores.keywords, 20],
          ["Skills", firstScores.skills, secondScores.skills, 20],
          ["Experience", firstScores.experience, secondScores.experience, 20],
          ["Projects", firstScores.projects, secondScores.projects, 20],
          ["Structure", firstScores.structure, secondScores.structure, 20],
          ["Overall ATS Score", firstScores.overall, secondScores.overall, 100]
        ]
      : [];

  return (
    <div>
      <h1>Compare Resumes</h1>
      <p className="page-subtitle">
        Compare two resumes using real ATS module scores.
      </p>

      <div className="compare-selector-card">
        <div>
          <label>Resume Version 1</label>
          <select value={firstId} onChange={(e) => setFirstId(e.target.value)}>
            <option value="">Select first resume</option>
            {resumes.map((resume) => (
              <option key={resume.id} value={resume.id}>
                {resume.fileName} - {resume.totalScore}/100
              </option>
            ))}
          </select>
        </div>

        <div className="vs-text">VS</div>

        <div>
          <label>Resume Version 2</label>
          <select value={secondId} onChange={(e) => setSecondId(e.target.value)}>
            <option value="">Select second resume</option>
            {resumes.map((resume) => (
              <option key={resume.id} value={resume.id}>
                {resume.fileName} - {resume.totalScore}/100
              </option>
            ))}
          </select>
        </div>
      </div>

      {firstResume && secondResume && (
        <div className="compare-dashboard">
          <div className="compare-visual-card">
            <h2>Score Comparison</h2>

            <div className="radar-wrapper">
              <ResponsiveContainer width="100%" height={360}>
                <RadarChart data={chartData}>
                  <PolarGrid />
                  <PolarAngleAxis
                    dataKey="dimension"
                    tick={{ fill: "#172554", fontSize: 13, fontWeight: 700 }}
                  />
                  <PolarRadiusAxis
                    angle={90}
                    domain={[0, 100]}
                    tick={{ fill: "#172554", fontSize: 12, fontWeight: 700 }}
                  />
                  <Radar
                    name="Resume 1"
                    dataKey="Resume1"
                    stroke="#2563eb"
                    fill="#2563eb"
                    fillOpacity={0.25}
                  />
                  <Radar
                    name="Resume 2"
                    dataKey="Resume2"
                    stroke="#7c3aed"
                    fill="#7c3aed"
                    fillOpacity={0.25}
                  />
                  <Legend />
                </RadarChart>
              </ResponsiveContainer>
            </div>
          </div>

          <div className="compare-summary-card">
            <h2>Score Summary</h2>

            <div className="summary-table">
              <div className="summary-header">
                <span>Dimension</span>
                <span>Resume 1</span>
                <span>Resume 2</span>
                <span>Change</span>
              </div>

              {summaryRows.map(([label, first, second, max]) => {
                const change = second - first;

                return (
                  <div className="summary-row" key={label}>
                    <span>{label}</span>
                    <span>{first}/{max}</span>
                    <span>{second}/{max}</span>
                    <span
                      className={
                        change >= 0 ? "positive-change" : "negative-change"
                      }
                    >
                      {change > 0 ? `+${change}` : `${change}`}
                    </span>
                  </div>
                );
              })}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default CompareResume;