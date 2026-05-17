import React, { useState } from "react";

function LoginPage({ onLoginSuccess }) {
  const [isLogin, setIsLogin] = useState(true);

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
  });

  const handleSubmit = async () => {
    if (!formData.email.trim()) {
  alert("Email is required");
  return;
}

if (!formData.email.includes("@")) {
  alert("Enter a valid email address");
  return;
}

if (!formData.password.trim()) {
  alert("Password is required");
  return;
}

if (formData.password.length < 6) {
  alert("Password must be at least 6 characters");
  return;
}

if (!isLogin && !formData.name.trim()) {
  alert("Name is required");
  return;
}


    const endpoint = isLogin ? "login" : "register";

    try {
      const response = await fetch(`http://localhost:8080/auth/${endpoint}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      const data = await response.json();

      if (!response.ok) {
        alert(data.message || "Authentication failed");
        return;
      }

      if (isLogin) {
  onLoginSuccess(data);
} else {
  alert("Registration successful. Please login now.");

  alert("Registration successful. Please login now.");

setIsLogin(true);

setFormData({
  name: "",
  email: formData.email.trim(),
  password: ""
});
}
    } catch (error) {
      console.error(error);
      alert("Something went wrong");
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h1>JobFlow AI</h1>

        <h2>{isLogin ? "Login" : "Register"}</h2>

        {!isLogin && (
          <input
            type="text"
            placeholder="Name"
            value={formData.name}
            onChange={(e) =>
              setFormData({
                ...formData,
                name: e.target.value,
              })
            }
          />
        )}

        <input
          type="email"
          placeholder="Email"
          value={formData.email}
          onChange={(e) =>
            setFormData({
              ...formData,
              email: e.target.value,
            })
          }
        />

        <input
          type="password"
          placeholder="Password"
          value={formData.password}
          onChange={(e) =>
            setFormData({
              ...formData,
              password: e.target.value,
            })
          }
        />

        <button onClick={handleSubmit}>
          {isLogin ? "Login" : "Register"}
        </button>

        <p>
          {isLogin ? "Don't have an account?" : "Already have an account?"}
          <span onClick={() => setIsLogin(!isLogin)}>
            {isLogin ? " Register" : " Login"}
          </span>
        </p>
      </div>
    </div>
  );
}

export default LoginPage;