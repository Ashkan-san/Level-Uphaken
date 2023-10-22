import React from "react";

export default function Card({ className, bodyClassName, title, children }) {
  return <div className={`card ${className}`}>
    {title && (
      <div className="card-header border-bottom">
        <h4 className="card-title">{title}</h4>
      </div>
    )}
    <div className="card-content">
      <div className={`card-body ${bodyClassName}`}>
        {children}
      </div>
    </div>
  </div>
}