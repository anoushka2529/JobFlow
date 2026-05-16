import { useState } from "react";
import axios from "axios";
import "./App.css";

function App() {
  const [file, setFile] = useState(null);
  const [jobDescription, setJobDescription] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!file) {
      alert("Please upload a resume PDF");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);
    formData.append("jobDescription", jobDescription);

    try {
      setLoading(true);

      const response = await axios.post(
        "http://localhost:8080/resume/upload",
        formData
      );

      setResult(response.data);
    } catch (error) {
      console.error(error);
      alert("Something went wrong while analyzing the resume");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app-container">
      <h1>JobFlow AI</h1>
      <p>Upload your resume and get AI-powered resume feedback.</p>

      <form onSubmit={handleSubmit} className="upload-form">
        <label>Upload Resume PDF</label>
        <input
          type="file"
          accept="application/pdf"
          onChange={(e) => setFile(e.target.files[0])}
        />

        <label>Paste Job Description</label>
        <textarea
          placeholder="Paste target job description here..."
          value={jobDescription}
          onChange={(e) => setJobDescription(e.target.value)}
        />

        <button type="submit" disabled={loading}>
          {loading ? "Analyzing..." : "Analyze Resume"}
        </button>
      </form>

      {result && (
        <div className="result-section">
          <h2>Parsed Resume Data</h2>
          <p><strong>Name:</strong> {result.resumeData?.name}</p>
          <p><strong>Email:</strong> {result.resumeData?.email}</p>
          <p><strong>Skills:</strong> {result.resumeData?.skills?.join(", ")}</p>

          <h2>ATS Score</h2>
          <p><strong>Total:</strong> {result.atsScore?.totalScore}/100</p>

          <h2>AI Analysis</h2>
          <pre>{result.aiAnalysis}</pre>
        </div>
      )}
    </div>
  );
}

export default App;