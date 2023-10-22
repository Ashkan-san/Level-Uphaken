import Head from 'next/head'
import { getSession } from 'next-auth/client'

export default function Home() {
  return (
    <>
      <Head>
        <title>Level Uphaken</title>
      </Head>
      <div className="page-heading">
        <div className="page-title">
          <div className="row">
            <div className="col mb-3">
              <h3>Willkommen bei <span style={{ color: '#17f4b3' }}>Level Uphaken!</span></h3>
              <h5 style={{ opacity: 0.7 }}>der ersten ToDo-App integriert mit der HAW</h5>
            </div>
          </div>
        </div>

        <section className="section row mb-5">

          <div className="col">
            <div className="card">
              <div className="card-header">
                <h4 className="card-title">Was dich erwartet:</h4>
              </div>
              <div className="card-body">
                <ul className="list">
                  <li><i className="bx bx-check-circle font-medium-3 me-50"></i>
                    Eine moderne, innovative ToDo-App mit Aufgaben deiner Module
                  </li>
                  <li><i className="bx bx-check-circle font-medium-3 me-50"></i>
                    Kombiniert mit spannenden, belohnenden Quests
                  </li>
                  <li><i className="bx bx-check-circle font-medium-3 me-50"></i>
                    sowie Übersicht über deinen bisherigen Fortschritt!
                  </li>
                  <br />
                  <li><i className="bx bx-check-circle font-medium-3 me-50"></i>
                    In Arbeit:
                    <br />Benachrichtigungen bei neuen Aufgaben
                    <br />Eine Menge neuer Quests
                    <br />Anschluss mit der Uni Hamburg und der TUHH
                  </li>
                </ul>
              </div>
            </div>

            <div className="card">
              <div className="card-header">
                <h4 className="card-title">Eure Entwickler:</h4>
              </div>
              <div className="card-body">
                <img src="/images/kjell.jpg" style={{ width: 150, height: 150 }} hspace="10" alt="Kjell" />
                <img src="/images/minh.jpg" style={{ width: 150, height: 150 }} hspace="10" alt="Minh" />
                <img src="/images/kat.jpg" style={{ width: 150, height: 150 }} hspace="10" alt="Kathleen" />
                <img src="/images/isabell.jpeg" style={{ width: 150, height: 150 }} hspace="10" alt="Isabell" />
                <img src="/images/ash.jpg" style={{ width: 150, height: 150 }} hspace="10" alt="Ash" />
                <img src="/images/philip.jpeg" style={{ width: 150, height: 150 }} hspace="10" alt="Philip" />
              </div>
            </div>

          </div>
          <div className="col">
              <div className="card">
                <div className="card-body">
                  <video autoplay controls muted src="/videos/Startseite_Video.mp4" type="video" width="100%" height="auto" preload="auto"></video>
                </div>
              </div>
          </div>
        </section>
      </div>
    </>
  )
}

export async function getServerSideProps(context) {
  const session = await getSession(context)

  if (session) {
    context.res.writeHead(302, { Location: '/user/dashboard' })
    context.res.end()
  }

  return { props: {} }
}
