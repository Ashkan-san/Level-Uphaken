import React from 'react'
import Head from 'next/head';
import Link from 'next/link';
import getConfig from 'next/config'
import swal from 'sweetalert'
import Card from '../../components/card'
import { useRouter } from 'next/router'
import { getSession } from 'next-auth/client'
import { requestAPI } from '../../utils/request'

export default function SubjectPage({ session, subject, profile }) {


  const { publicRuntimeConfig } = getConfig()
  const router = useRouter()

  const  tasks = subject.memos.flatMap(memo => memo.tasks.map(task => ({ memoId: memo.id, ...task })))
  const openTasks = tasks.filter(task => !profile.finishedTasks.some(elm => elm.id === task.id))
  let progress = (tasks.filter(task => profile.finishedTasks.some(elm => elm.id === task.id)).length / tasks.length) * 100
  if (isNaN(progress)) {
    progress = 0
  }

  function MemoCreator({ subject }) {


    async function handleAddMemo(e) {
      e.preventDefault();

      try {
        const newMemo = await requestAPI(`${publicRuntimeConfig.API_ENDPOINT}/memos/`, session, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            subject: {
              id: subject.id,
            },
            title: e.target.title.value,
            rewardXP: parseInt(e.target.rewardXP.value),
            description: e.target.description.value,
          })
        })
        router.push(`/memo/${newMemo.id}`)
      } catch (error) {
        console.error(error)
        swal({
          title: 'Fehlerhafte Eingabe!',
          text: error.data,
          icon: 'error',
        })
      }
    }

    return (
      <div className="card widget-todo">
        <div className="card-header border-bottom">
          <h4 className="card-title">Neues Memo anlegen</h4>
        </div>
        <div className="card-body p-4">
          <form onSubmit={handleAddMemo}>
            <div className="row">
              <div className="col-12 form-group mb-2">
                <label htmlFor="title">Titel</label>
                <input type="text" className='form-control' id="title" placeholder="Titel" required />
              </div>
              <div className="col-12 form-group mb-2">
                <label htmlFor="rewardXP">XP</label>
                <input type="number" className='form-control' id="rewardXP" defaultValue="50" min="0" max="200" required />
              </div>
              <div class="col-12 form-group mb-2">
                <label for="description" class="form-label">Beschreibung</label>
                <textarea class="form-control" id="description" rows="3"></textarea>
              </div>
              <div className="col-6 offset-3 mb-2">
                <button className="btn btn-primary btn-block btn-lg shadow-lg mt-5">Hinzufügen</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    )
  }


  return <>
    <Head>
      <title>Level Uphaken - {subject.acronym}</title>
    </Head>
    <div className="page-heading">
      <div className="page-title">
        <div className="row">
          <h3 align="center">{subject.subjectName} - &Uuml;bersicht</h3>
        </div>
      </div>
    </div>
    <div className="page-content">
      <section className="section row m-3">
        <div className="col">
          <Card title={`${profile.role !== 'PROFESSOR' ? 'Anstehende ' : ''}Aufgaben`}>
            {(openTasks.length > 0 && (
              <div className="list-group" role="tablist">
                {openTasks.map(task =>
                  <Link key={task.id} href={`/memo/${task.memoId}`}>
                    <a className="list-group-item list-group-item-action" role="tab" aria-selected="false">
                      {task.title}
                    </a>
                  </Link>
                )}
              </div>
            )) || (
                <div className="text-center">
                  Alles erledigt 🥳
                </div>
              )}

          </Card>

          {profile.role !== 'PROFESSOR' && (
            <div className="card">
              <div className="card-body mt-5">
                <div className="progress progress-primary mb-4" style={{ width: '80%' }}>
                  <div className="progress-bar progress-label" role="progressbar" style={{ width: `${progress}%` }} aria-valuenow={parseFloat(progress).toFixed(2)} aria-valuemin="0" aria-valuemax="100"></div>
                </div>
              </div>
            </div>
          )}

          {profile.role === 'PROFESSOR' && <MemoCreator subject={subject} />}


        </div>
        <div className="col">
          <div className="row">
            <div className="card">
              <div className="card-header border-bottom mb-3">
                <h4 className="card-title">Infos &uuml;ber den Kurs</h4>
              </div>
              <div className="card-body" dangerouslySetInnerHTML={{ __html: subject.description }}></div>
            </div>
          </div>
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

  const { subjectId } = context.query;
  try {
    const subject = await requestAPI(`${process.env.API_ENDPOINT}/subjects/${subjectId}`, session)

    // Check if User is enrolled
    if (subject.prof.emailAddress !== session.user.email && !subject.users.some(user => user.emailAddress === session.user.email)) {
      return {
        redirect: {
          destination: '/subject/notEnrolled',
          permanent: false,
        }
      }
    }

    return {
      props: { session, subject }
    }

  } catch (error) {
    if (error.status && error.status === 403) {
      return {
        redirect: {
          destination: '/auth/logout',
          permanent: false,
        }
      }
    }

    return {
      redirect: {
        destination: '/subject/notFound',
        permanent: false,
      }
    }
  }
}