package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants() {
		Collection<Participant> participants = participantService.getAll();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		Participant participantToCheck = participantService.findByLogin(participant.getLogin());
		if (participantToCheck == null) {
			participantService.addParticipant(participant);
			return new ResponseEntity("Participant added", HttpStatus.OK);
		}
		return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeParticipant(@PathVariable("id")  String login) {
		Participant participantToCheck = participantService.findByLogin(login);
		if (participantToCheck == null) {
			return new ResponseEntity("Unable to remove. A participant with login " + login + " does not exist.", HttpStatus.NOT_FOUND);
		}
		participantService.removeParticipant(participantToCheck);
		return new ResponseEntity("Participant removed", HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@PathVariable("id")  String login, @RequestBody Participant updatedParticipant) {
		Participant participantToCheck = participantService.findByLogin(login);
		if (participantToCheck == null) {
			return new ResponseEntity("Unable to update. A participant with login " + login + " does not exist.", HttpStatus.NOT_FOUND);
		}
		participantService.updateParticipant(participantToCheck, updatedParticipant);
		return new ResponseEntity("Participant updated", HttpStatus.OK);
	}

}
