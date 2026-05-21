import React from "react";

function Profile({ user }) {
  return (
    <div>
      <h1>Profile</h1>
      <p className="page-subtitle">Your basic account details.</p>

      <div className="profile-card">
        <div className="profile-left">
          <div className="profile-avatar">
            {user.name ? user.name.charAt(0).toUpperCase() : "U"}
          </div>
          <h2>{user.name || "User"}</h2>
          <p>{user.email || "Not available"}</p>
        </div>

        <div className="profile-right">
          <div className="profile-row">
            <span>Name</span>
            <p>{user.name || "Not available"}</p>
          </div>

          <div className="profile-row">
            <span>Email</span>
            <p>{user.email || "Not available"}</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Profile;