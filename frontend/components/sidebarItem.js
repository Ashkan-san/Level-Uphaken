import Link from "next/link";
import React from "react";
import { useState } from 'react'

export function SidebarItem({ subject }) {
  let memos = subject.memos;
  const [open, setOpen] = useState(false)

  /**
   * Ganz allgemein gsprochen:
   *
   * 3 Styling Probleme
   * * einaufklappbar
   * * Maus ist ein Zeiger anstatt Hand (wenn man über einen link hovert ändert sich die Maus meistens)
   * * Verlinkung
   *
   * nur <a> sieht richtig gestyled aus
   *
   * wenn man sich entschließt das <a> nicht mit class=sidebar-link auszustatten gibt es einen runtime error
   *
   * wenn <a> kein href hat, ist die Maus keine Hand sondern ein normaler Zeiger
   *
   * wenn man ein <Link> um <a> macht, ist die Maus ein Zeiger
   * aber man wird nicht auf den Link weitergeleitet
   * ! selbst wenn das <a> auch ein href hat
   *
   *  * es macht keinen Unterschied dass <Link> class=sidebar-link ist
   * im Endeffekt forciert Mazer dass sidebar-link in <a> steht
   *
   * Fazit: href in Link wird hier aus unerfindlichen Gründen ignoriert
   * und <a> trägt die Mazer css classes
   *
   * Deswegen: gibt es extra einen "Startseite" Tab für ein Fach
   * steht als href ein leerer String (bedeutet defaultmäßig aktuelle Seite)
   */

  return (
    <li key={subject.id} className="sidebar-item has-sub">
      <a className="sidebar-link" onClick={() => setOpen(!open)}>
        <i className="bi bi-stack" />
        <span>{subject.acronym}</span>
      </a>

      <ul className={"submenu " + open ? "active" : ""} style={{ display: open ? "block" : "none" }}>
        <li key={0} className="submenu-item" style={{ listStyle: 'none' }}>
          <Link href={`/subject/${subject.id}`}>
            <a className="sidebar-link">
              Startseite
            </a>
          </Link>
        </li>
        {memos.map(memo => 
          <li key={memo.id} className="submenu-item" style={{ listStyle: 'none' }}>
            <Link href={`/memo/${memo.id}`}>
              <a className="sidebar-link">{memo.title}</a>
            </Link>
          </li>
        )}
      </ul>
    </li>
  )
}