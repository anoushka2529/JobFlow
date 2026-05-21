import React, { useState } from "react";
import Dashboard from "./Dashboard";
import AnalyzeResume from "./AnalyzeResume";
import MyResumes from "./MyResumes";
import InterviewPractice from "./InterviewPractice";
import Reports from "./Reports";
import CompareResume from "./CompareResume";
import SavedQuestions from "./SavedQuestions";
import Profile from "./Profile";

function HomePage({ user, onLogout }) {
  const [activePage, setActivePage] = useState("dashboard");

  const renderPage = () => {
    switch (activePage) {
      case "dashboard":
        return <Dashboard user={user} />;
      case "analyze":
        return <AnalyzeResume user={user} />;
      case "myResumes":
        return <MyResumes user={user} />;
      case "interview":
        return <InterviewPractice user={user} />;
      case "reports":
        return <Reports user={user} />;
      case "compare":
        return <CompareResume user={user} />;
      case "saved":
        return <SavedQuestions user={user} />;
      case "profile":
        return <Profile user={user} />;
      default:
        return <Dashboard user={user} />;
    }
  };

  return (
    <div className="jobflow-layout">
      <aside className="sidebar">
        <h2 className="logo">JobFlow</h2>

        <button onClick={() => setActivePage("dashboard")}>Dashboard</button>
        <button onClick={() => setActivePage("analyze")}>Analyze Resume</button>
        <button onClick={() => setActivePage("myResumes")}>My Resumes</button>
        <button onClick={() => setActivePage("interview")}>Interview Practice</button>
        <button onClick={() => setActivePage("reports")}>Reports</button>
        <button onClick={() => setActivePage("compare")}>Compare Resume</button>
        <button onClick={() => setActivePage("saved")}>Saved Questions</button>
        <button onClick={() => setActivePage("profile")}>Profile</button>
        <button className="logout-btn" onClick={onLogout}>
          Logout
        </button>
      </aside>

      <main className="main-content">{renderPage()}</main>
    </div>
  );
}

export default HomePage;