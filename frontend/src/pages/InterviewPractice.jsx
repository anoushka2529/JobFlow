import React, { useEffect, useState } from "react";

function InterviewPractice({ user }) {
  const [questions, setQuestions] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [answer, setAnswer] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetch(`http://localhost:8080/resume/history/${user.userId}`)
      .then((res) => res.json())
      .then((data) => {
        if (!Array.isArray(data) || data.length === 0) {
          setQuestions([]);
          return;
        }

        const latestResume = data[data.length - 1];

        const extractedQuestions = latestResume.interviewQuestions
  ? latestResume.interviewQuestions
      .split("\n")
      .map((q) =>
        q.replace(/^\d+[\).\s-]*/, "")
         .replace(/^[-•]\s*/, "")
         .trim()
      )
      .filter(
        (q) =>
          q.length > 0 &&
          q.includes("?") && // only keep actual questions
          !q.toLowerCase().includes("technical questions") &&
          !q.toLowerCase().includes("project-based questions") &&
          !q.toLowerCase().includes("behavioral questions") &&
          !q.toLowerCase().includes("system design questions")
      )
  : [];

        setQuestions(extractedQuestions);
      })
      .catch((err) => {
        console.error("Interview questions error:", err);
        setQuestions([]);
      });
  }, [user.userId]);

  const currentQuestion = questions[currentIndex];

  const handleSubmit = async () => {
    if (!answer.trim()) {
      alert("Please type your answer first");
      return;
    }

    try {
      setLoading(true);

      const response = await fetch("http://localhost:8080/interview/evaluate", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          question: currentQuestion,
          answer: answer
        })
      });

      const data = await response.json();
      setResult(data);
    } catch (error) {
      console.error(error);
      alert("Something went wrong while evaluating your answer");
    } finally {
      setLoading(false);
    }
  };

  const handleNext = () => {
    setCurrentIndex((prev) => (prev + 1) % questions.length);
    setAnswer("");
    setResult(null);
  };

  if (questions.length === 0) {
    return (
      <div>
        <h1>Interview Practice</h1>
        <p className="page-subtitle">
          No personalized questions found. Analyze a resume first.
        </p>
      </div>
    );
  }

  return (
    <div>
      <h1>Interview Practice</h1>
      <p className="page-subtitle">
        Practicing questions from your most recent resume analysis.
      </p>

      <div className="interview-card">
        <span className="question-count">
          Question {currentIndex + 1} of {questions.length}
        </span>

        <h2>{currentQuestion}</h2>

        <textarea
          placeholder="Type your answer here..."
          value={answer}
          onChange={(e) => setAnswer(e.target.value)}
        />

        <div className="interview-actions">
          <button onClick={handleSubmit} disabled={loading}>
            {loading ? "Evaluating..." : "Submit Answer"}
          </button>

          <button className="secondary-btn" onClick={handleNext}>
            Next Question
          </button>
        </div>
      </div>

      {result && (
        <div className="feedback-card">
          <h2>AI Feedback</h2>
          <div className="feedback-score">{result.score}/10</div>
          <p className="feedback-text">{result.feedback}</p>
        </div>
      )}
    </div>
  );
}

export default InterviewPractice;