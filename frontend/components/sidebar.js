import React from 'react'
import Link from 'next/link'
import { SidebarItem } from "./sidebarItem"

export default function Sidebar({ session, profile }) {

  let subjects = []
  if (profile.subjects && profile.subjects.length > 0) {
    subjects = profile.subjects
  } else if (profile.profSubjects && profile.profSubjects.length > 0) {
    subjects = profile.profSubjects
  }

  // wird ganz unten eingebunden in ein <ul>
  let sidebarContent

  // Wenn keiner angemeldet ist..
  if (!session) {
    sidebarContent = <>
      <li className="sidebar-item">
        <Link href="/">
          <a className="sidebar-link">
            <i className="bi bi-grid-fill"></i>
            <span>Startseite</span>
          </a>
        </Link>
      </li>
      <li className="sidebar-item">
        <Link href="/auth/login">
          <a className="sidebar-link">
            <i className="bi bi-person-circle"></i>
            <span>Anmelden</span>
          </a>
        </Link>
      </li>
      <li className="sidebar-item">
        <Link href="/auth/register">
          <a className="sidebar-link">
            <i className="bi bi-person-plus-fill"></i>
            <span>Registrieren</span>
          </a>
        </Link>
      </li>
    </>
  } else {
    // Das was schon vorher hier war
    sidebarContent = <>
      <li className="sidebar-item">
        <Link href="/user/dashboard">
          <a className="sidebar-link">
            <i className="bi bi-house-fill"></i>
            <span>Dashboard</span>
          </a>
        </Link>
      </li>
      <li className="sidebar-item">
        <Link href="/subject/">
          <a className="sidebar-link">
            <i className="bi bi-grid-fill"></i>
            <span>Kurs&uuml;bersicht</span>
          </a>
        </Link>
      </li>
      {subjects.map(subject =>
        <SidebarItem key={subject.id} subject={subject} />)
      }
    </>
  }

  return (
    <div id="sidebar" className="active">
      <div className="sidebar-wrapper active">
        <div className="sidebar-header">
          <div className="d-flex justify-content-center">
            <div className="logo">
              <a href="/"><img src="/images/logo.png" alt="Level Uphaken" srcSet="" /></a>
            </div>
            <div className="toggler">
              <a href="#" className="sidebar-hide d-xl-none d-block"><i
                className="bi bi-x bi-middle" /></a>
            </div>
          </div>
        </div>
        <div className="sidebar-menu">
          <ul className="menu">
            {sidebarContent}
          </ul>
        </div>
        <button className="sidebar-toggler btn x"><i data-feather="x" /></button>
      </div>
    </div>
  );
}