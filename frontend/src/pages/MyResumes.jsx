import React, { useEffect, useState } from "react";

function MyResumes({
    user,
    setSelectedResume,
    setActivePage
}) {
  const [resumes, setResumes] = useState([]);

  useEffect(() => {
    fetch(`http://localhost:8080/resume/history/${user.userId}`)
      .then((res) => res.json())
      .then((data) => setResumes(Array.isArray(data) ? data : []))
      .catch((err) => {
        console.error("My Resumes error:", err);
        setResumes([]);
      });
  }, [user.userId]);

  return (
    <div>
      <h1>My Resumes</h1>
      <p className="page-subtitle">
        View all resumes you have analyzed using JobFlow.
      </p>

      <div className="module-card">
        {resumes.length === 0 ? (
          <p>No resumes analyzed yet.</p>
        ) : (
          <div className="resume-table">
            <div className="resume-table-header">
              <span>File Name</span>
              <span>Candidate Name</span>
              <span>ATS Score</span>
              <span>Date</span>
            </div>

            {resumes
              .slice()
              .reverse()
              .map((resume) => (
                <div
  className="resume-table-row clickable-row"
  key={resume.id}
  onClick={() => {
    setSelectedResume(resume);
    setActivePage("analyze");
  }}
>
                  <span>{resume.fileName}</span>
                  <span>{resume.candidateName || "Not found"}</span>
                  <span>{resume.totalScore}</span>
                  <span>
                    {resume.createdAt
                      ? new Date(resume.createdAt).toLocaleDateString()
                      : "N/A"}
                  </span>
                </div>
              ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default MyResumes;