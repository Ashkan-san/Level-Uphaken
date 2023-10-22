import Head from "next/head";
import React, { useState } from "react";
import SubjectCard from "../../components/subjectCard";
import { getSession } from "next-auth/client";
import { requestAPI } from '../../utils/request'
import fuzzysort from 'fuzzysort'

export default function SubjectIndex({ subjects, session, profile }) {

  const [filteredSubjects, setFilteredSubjects] = useState(subjects)
  function handleSearch(event) {
    const { value } = event.target;
    if (!value || value === '') {
      setFilteredSubjects(subjects)
      return
    }
    setFilteredSubjects(fuzzysort.go(value, subjects, { keys: ['subjectName', 'acronym', 'prof.emailAddress', 'prof.lastName'], allowTypo: true }).map(result => result.obj));
  }

  return <>
    <Head>
      <title>Kurssuche</title>
    </Head>
    <div className="page-heading">
      <div className="page-title">
        <div className="row">
          <h3 align="center">Kurs&uuml;bersicht</h3>
        </div>
      </div>
      <div className="input-group mb-3">
        <input type="text"
          className="form-control"
          placeholder="Suche nach Fachnamen, Professor etc." aria-label="Suche f&uuml; Fach"
          onChange={handleSearch} />
      </div>
    </div>
    <div className="page-content">
      <section className="section">
        <div className="row match-height">
          {filteredSubjects.map(subject => <SubjectCard key={subject.id} subject={subject} session={session} member={profile.subjects && profile.subjects.some(elm => elm.acronym === subject.acronym)} />)}
          {filteredSubjects.length === 0 && (
            <div className="offset-4 col-4 card text-center">
              <div className="card-title p-3">
                Keine Fächer gefunden!
              </div>
            </div>
          )}
        </div>
      </section>
    </div>
  </>
}

export async function getServerSideProps(context) {
  const session = await getSession(context)

  if (!session) {
    return {
      redirect: {
        destination: '/auth/login',
        permanent: false,
      }
    }
  }

  try {
    const subjects = await requestAPI(`${process.env.API_ENDPOINT}/subjects/`, session)

    return {
      props: {
        subjects
      }
    }
  } catch (error) {
    console.error(error)
    return {
      redirect: {
        destination: '/auth/logout',
        permanent: false,
      }
    }
  }

}