import React, { useEffect, useState } from "react";

function Dashboard({ user }) {
  const [history, setHistory] = useState([]);

  const userId =user?.userId;

  useEffect(() => {
    console.log("Logged in user full:", JSON.stringify(user));
    console.log("Resolved userId:", userId);

    if (!userId) {
      console.error("User ID missing");
      setHistory([]);
      return;
    }

    fetch(`http://localhost:8080/resume/history/${userId}`)
      .then((res) => res.json())
      .then((data) => {
        console.log("History data:", data);
        setHistory(Array.isArray(data) ? data : []);
      })
      .catch((err) => {
        console.error("Dashboard error:", err);
        setHistory([]);
      });
  }, [userId]);
  const totalResumes = history.length;

  const bestScore =
    history.length > 0
      ? Math.max(...history.map((item) => item.totalScore || 0))
      : 0;

  const latestScore =
    history.length > 0 ? history[history.length - 1].totalScore : 0;

  return (
    <div>
      <h1>Welcome back, {user?.name || "User"}</h1>
      <p className="page-subtitle">Here is your JobFlow overview.</p>

      <div className="dashboard-grid">
        <div className="dashboard-card">
          <h3>Total Resumes</h3>
          <p>{totalResumes}</p>
        </div>

        <div className="dashboard-card">
          <h3>Best ATS Score</h3>
          <p>{bestScore}</p>
        </div>

        <div className="dashboard-card">
          <h3>Latest ATS Score</h3>
          <p>{latestScore}</p>
        </div>

        <div className="dashboard-card">
          <h3>Saved Questions</h3>
          <p>0</p>
        </div>
      </div>

      <div className="module-card">
        <h2>Recent Resume Analyses</h2>

        {history.length === 0 ? (
          <p>No resumes analyzed yet.</p>
        ) : (
          <div className="history-list">
            {history.slice(-5).reverse().map((item) => (
              <div className="history-item" key={item.id}>
                <span>{item.fileName}</span>
                <strong>ATS Score: {item.totalScore}</strong>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default Dashboard;